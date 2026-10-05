package com.icinema.theatre.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "SEATING-SERVICE")
public interface SeatingClient {

    @PostMapping("/api/seats/generate")
    List<Map<String, Object>> generateSeats(
            @RequestParam Long screenId,
            @RequestParam int totalSeats,
            @RequestParam double price
    );

    @PutMapping("/api/seats/screen/{screenId}/resize")
    List<Map<String, Object>> resizeSeats(
            @PathVariable Long screenId,
            @RequestParam int totalSeats,
            @RequestParam double price
    );
}