package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingClient {

    @GetMapping("/api/bookings/occupied-seats")
    List<Long> getOccupiedSeatIds(
            @RequestParam Long screenId,
            @RequestParam LocalDateTime showTime
    );
}