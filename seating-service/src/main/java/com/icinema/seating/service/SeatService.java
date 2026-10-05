package com.icinema.seating.service;

import org.springframework.stereotype.Service;

import com.icinema.seating.repository.SeatRepository;
import com.icinema.seating.entity.Seat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public List<Seat> getSeatsByScreenId(Long screenId) {
        return seatRepository.findByScreenId(screenId);
    }

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = getSeatById(id);

        seat.setScreenId(seatDetails.getScreenId());
        seat.setSeatNumber(seatDetails.getSeatNumber());
        seat.setSeatType(seatDetails.getSeatType());
        seat.setPrice(seatDetails.getPrice());
        seat.setStatus(seatDetails.getStatus());

        return seatRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat not found");
        }

        seatRepository.deleteById(id);
    }

    public List<Seat> generateSeats(
            Long screenId,
            int totalSeats,
            double price) {

        if (totalSeats <= 0) {
            throw new RuntimeException("Total seats must be greater than zero");
        }

        List<Seat> seats = new ArrayList<>();

        int seatsPerRow = 10;

        for (int i = 0; i < totalSeats; i++) {

            int row = i / seatsPerRow;
            int seatNumber = (i % seatsPerRow) + 1;

            char rowName = (char) ('A' + row);

            Seat seat = new Seat();
            seat.setScreenId(screenId);
            seat.setSeatNumber(rowName + String.valueOf(seatNumber));
            seat.setSeatType("STANDARD");
            seat.setPrice(price);
            seat.setStatus("AVAILABLE");

            seats.add(seat);
        }

        return seatRepository.saveAll(seats);
    }

    public List<Seat> resizeSeats(
            Long screenId,
            int newTotalSeats,
            double price) {

        if (newTotalSeats <= 0) {
            throw new RuntimeException("Total seats must be greater than zero");
        }

        List<Seat> existingSeats = seatRepository.findByScreenId(screenId);

        int currentTotalSeats = existingSeats.size();

        if (newTotalSeats == currentTotalSeats) {
            return existingSeats;
        }

        if (newTotalSeats > currentTotalSeats) {

            List<Seat> newSeats = new ArrayList<>();

            int seatsPerRow = 10;

            for (int i = currentTotalSeats; i < newTotalSeats; i++) {

                int row = i / seatsPerRow;
                int seatNumber = (i % seatsPerRow) + 1;

                char rowName = (char) ('A' + row);

                Seat seat = new Seat();
                seat.setScreenId(screenId);
                seat.setSeatNumber(rowName + String.valueOf(seatNumber));
                seat.setSeatType("STANDARD");
                seat.setPrice(price);
                seat.setStatus("AVAILABLE");

                newSeats.add(seat);
            }

            seatRepository.saveAll(newSeats);

        } else {

            List<Seat> seatsToRemove = existingSeats.stream()
                    .sorted(
                            Comparator
                                    .comparingInt((Seat seat) -> getSeatPosition(seat.getSeatNumber()))
                                    .reversed()
                    )
                    .limit(currentTotalSeats - newTotalSeats)
                    .toList();

            boolean hasBookedSeats = seatsToRemove.stream()
                    .anyMatch(seat -> !"AVAILABLE".equalsIgnoreCase(seat.getStatus()));

            if (hasBookedSeats) {
                throw new RuntimeException(
                        "Cannot reduce capacity because some seats are already booked or unavailable"
                );
            }

            seatRepository.deleteAll(seatsToRemove);
        }

        return seatRepository.findByScreenId(screenId);
    }

    private int getSeatPosition(String seatNumber) {

        if (seatNumber == null || seatNumber.length() < 2) {
            return 0;
        }

        char row = seatNumber.charAt(0);
        int number = Integer.parseInt(seatNumber.substring(1));

        return ((row - 'A') * 100) + number;
    }
}