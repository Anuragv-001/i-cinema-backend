package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "SEATING-SERVICE")
public interface SeatClient {

    @GetMapping("/api/seats/screen/{screenId}")
    List<Map<String, Object>> getSeatsByScreenId(
            @PathVariable Long screenId
    );
}