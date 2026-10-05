package com.icinema.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@FeignClient(
        name = "BOOKING-SERVICE",
        contextId = "bookingClient"
)
public interface BookingClient {

    @GetMapping("/api/bookings")
    List<Map<String, Object>> getBookings();

    @GetMapping("/api/bookings/{id}")
    Map<String, Object> getBooking(
            @PathVariable Long id
    );

    @GetMapping("/api/bookings/booking-id/{bookingId}")
    Map<String, Object> getBookingByBookingId(
            @PathVariable String bookingId
    );

    @GetMapping("/api/bookings/user")
    List<Map<String, Object>> getBookingsByUser(
            @RequestParam String email
    );

    @GetMapping("/api/bookings/booking-id/{bookingId}/seats")
    List<Map<String, Object>> getBookingSeats(
            @PathVariable String bookingId
    );

    @PutMapping("/api/bookings/{id}/status")
    Map<String, Object> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    );

    @PutMapping("/api/bookings/{id}/cancel")
    String cancelBooking(
            @PathVariable Long id
    );

    @GetMapping("/api/bookings/occupied-seats")
    List<Long> getOccupiedSeatIds(
            @RequestParam Long screenId,
            @RequestParam LocalDateTime showTime
    );
}