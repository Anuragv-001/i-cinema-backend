package com.icinema.booking.service;

import com.icinema.booking.dto.BookingRequest;
import com.icinema.booking.dto.NotificationRequest;
import com.icinema.booking.entity.Booking;
import com.icinema.booking.entity.BookingSeat;
import com.icinema.booking.repository.BookingRepository;
import com.icinema.booking.repository.BookingSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import com.icinema.booking.client.NotificationClient;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final NotificationClient notificationClient;

    public BookingService(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            NotificationClient notificationClient) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.notificationClient = notificationClient;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking getBookingByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }

    @Transactional
    public Booking createBooking(BookingRequest request) {

        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new RuntimeException("At least one seat is required");
        }

        Set<Long> requestedSeatIds = new HashSet<>(request.getSeatIds());

        if (requestedSeatIds.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Duplicate seats selected");
        }

        List<Booking> existingBookings =
                bookingRepository.findByScreenIdAndShowTimeAndStatusIn(
                        request.getScreenId(),
                        request.getShowTime(),
                        List.of("CONFIRMED", "PENDING_PAYMENT")
                );

        Set<Long> bookedSeatIds = new HashSet<>();

        LocalDateTime now = LocalDateTime.now();

        for (Booking existingBooking : existingBookings) {

            boolean seatLocked = false;

            if ("CONFIRMED".equals(existingBooking.getStatus())) {
                seatLocked = true;
            }

            if ("PENDING_PAYMENT".equals(existingBooking.getStatus())
                    && existingBooking.getPaymentExpiry() != null
                    && existingBooking.getPaymentExpiry().isAfter(now)) {
                seatLocked = true;
            }

            if (!seatLocked) {
                continue;
            }

            List<BookingSeat> existingSeats =
                    bookingSeatRepository.findByBookingId(
                            existingBooking.getBookingId()
                    );

            for (BookingSeat existingSeat : existingSeats) {
                bookedSeatIds.add(existingSeat.getSeatId());
            }
        }

        for (Long seatId : requestedSeatIds) {
            if (bookedSeatIds.contains(seatId)) {
                throw new RuntimeException(
                        "Seat " + seatId + " is already booked or temporarily locked"
                );
            }
        }

        String bookingId =
                "ICN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        Booking booking = new Booking();

        booking.setBookingId(bookingId);
        booking.setUserEmail(request.getUserEmail());
        booking.setMovieId(request.getMovieId());
        booking.setTheatreId(request.getTheatreId());
        booking.setScreenId(request.getScreenId());
        booking.setShowTime(request.getShowTime());
        booking.setTicketPrice(request.getTicketPrice());
        booking.setConvenienceFee(request.getConvenienceFee());
        booking.setTotalAmount(request.getTotalAmount());

        booking.setStatus("PENDING_PAYMENT");
        booking.setBookingTime(now);
        booking.setPaymentExpiry(now.plusMinutes(10));

        Booking savedBooking = bookingRepository.save(booking);

        for (Long seatId : requestedSeatIds) {

            BookingSeat bookingSeat = new BookingSeat();

            bookingSeat.setBookingId(savedBooking.getBookingId());
            bookingSeat.setSeatId(seatId);
            bookingSeat.setSeatNumber("SEAT-" + seatId);

            bookingSeatRepository.save(bookingSeat);
        }

        return savedBooking;
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        Booking savedBooking = bookingRepository.save(booking);
        if("CONFIRMED".equalsIgnoreCase(status)){
            sendBookingConfirmationNotification(savedBooking);
        }
        return savedBooking;
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus("CANCELLED");
        Booking savedBooking = bookingRepository.save(booking);
        sendBookingCancellationNotification(savedBooking);
    }

    public List<BookingSeat> getBookingSeats(String bookingId) {
        return bookingSeatRepository.findByBookingId(bookingId);
    }

    public List<Long> getOccupiedSeatIds(
            Long screenId,
            LocalDateTime showTime) {

        List<Booking> existingBookings =
                bookingRepository.findByScreenIdAndShowTimeAndStatusIn(
                        screenId,
                        showTime,
                        List.of("CONFIRMED", "PENDING_PAYMENT")
                );

        Set<Long> occupiedSeatIds = new HashSet<>();

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : existingBookings) {

            boolean seatLocked = false;

            if ("CONFIRMED".equals(booking.getStatus())) {
                seatLocked = true;
            }

            if ("PENDING_PAYMENT".equals(booking.getStatus())
                    && booking.getPaymentExpiry() != null
                    && booking.getPaymentExpiry().isAfter(now)) {
                seatLocked = true;
            }

            if (!seatLocked) {
                continue;
            }

            List<BookingSeat> seats =
                    bookingSeatRepository.findByBookingId(
                            booking.getBookingId()
                    );

            for (BookingSeat seat : seats) {
                occupiedSeatIds.add(seat.getSeatId());
            }
        }

        return occupiedSeatIds.stream().toList();
    }

    @Scheduled(fixedRate=60000)
    @Transactional
    public void expirePendingBookings(){
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepository.findByStatusAndPaymentExpiryBefore("PENDING_PAYMENT", now);

        for(Booking booking : expiredBookings){
            booking.setStatus("EXPIRED");
            bookingRepository.save(booking);
        }
    }

    private void sendBookingConfirmationNotification(Booking booking) {
        NotificationRequest request = new NotificationRequest(
            booking.getUserEmail(),
            booking.getBookingId(),
            "BOOKING_CONFIRMATION",
            "I-Cinema Booking Confirmed",
            "Your I-Cinema booking "+booking.getBookingId()+" has been confirmed successfully."
        );
        try{
            notificationClient.sendNotification(request);
        }catch(Exception e){
            System.out.println("Booking confirmed but notification could not be sent: " + e.getMessage());
        }

    }

    private void sendBookingCancellationNotification(Booking booking) {

        NotificationRequest request = new NotificationRequest(
                booking.getUserEmail(),
                booking.getBookingId(),
                "BOOKING_CANCELLATION",
                "I-Cinema Booking Cancelled",
                "Your I-Cinema booking "
                        + booking.getBookingId()
                        + " has been cancelled successfully."
        );

        try {
            notificationClient.sendNotification(request);
        } catch (Exception e) {
            System.out.println(
                    "Booking cancelled, but notification could not be sent: "
                            + e.getMessage()
            );
        }
    }
}