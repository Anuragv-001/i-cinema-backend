package com.icinema.admin.controller;

import com.icinema.admin.client.BookingClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingClient bookingClient;

    public AdminBookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @GetMapping
    public List<Map<String, Object>> getBookings() {
        return bookingClient.getBookings();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getBooking(
            @PathVariable Long id) {
        return bookingClient.getBooking(id);
    }

    @GetMapping("/booking-id/{bookingId}")
    public Map<String, Object> getBookingByBookingId(
            @PathVariable String bookingId) {
        return bookingClient.getBookingByBookingId(bookingId);
    }

    @GetMapping("/user")
    public List<Map<String, Object>> getBookingsByUser(
            @RequestParam String email) {
        return bookingClient.getBookingsByUser(email);
    }

    @GetMapping("/booking-id/{bookingId}/seats")
    public List<Map<String, Object>> getBookingSeats(
            @PathVariable String bookingId) {
        return bookingClient.getBookingSeats(bookingId);
    }

    @PutMapping("/{id}/status")
    public Map<String, Object> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        return bookingClient.updateBookingStatus(id, request);
    }

    @PutMapping("/{id}/cancel")
    public String cancelBooking(
            @PathVariable Long id) {
        return bookingClient.cancelBooking(id);
    }
}