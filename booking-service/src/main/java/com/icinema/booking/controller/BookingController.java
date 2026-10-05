package com.icinema.booking.controller;

import com.icinema.booking.dto.BookingRequest;
import com.icinema.booking.entity.Booking;
import com.icinema.booking.entity.BookingSeat;
import com.icinema.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
// @CrossOrigin("http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.getBookingById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/booking-id/{bookingId}")
    public ResponseEntity<?> getBookingByBookingId(
            @PathVariable String bookingId) {
        try {
            return ResponseEntity.ok(
                    bookingService.getBookingByBookingId(bookingId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getBookingsByUser(
            @RequestParam String email) {
        return ResponseEntity.ok(
                bookingService.getBookingsByUser(email)
        );
    }

    @GetMapping("/booking-id/{bookingId}/seats")
    public ResponseEntity<List<BookingSeat>> getBookingSeats(
            @PathVariable String bookingId) {
        return ResponseEntity.ok(
                bookingService.getBookingSeats(bookingId)
        );
    }

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bookingService.createBooking(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(
                    bookingService.updateBookingStatus(
                            id,
                            request.get("status")
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/occupied-seats")
    public ResponseEntity<List<Long>> getOccupiedSeatIds(
            @RequestParam Long screenId,
            @RequestParam LocalDateTime showTime) {

        return ResponseEntity.ok(
                bookingService.getOccupiedSeatIds(
                        screenId,
                        showTime
                )
        );
    }
}