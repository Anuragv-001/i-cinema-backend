package com.icinema.admin.client;

import com.icinema.admin.dto.TheatreRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "THEATRE-SERVICE", contextId = "theatreClient")
public interface TheatreClient {
    @GetMapping("/api/theatres")
    List<Map<String, Object>> getTheatres();

    @GetMapping("/api/theatres/{id}")
    Map<String, Object> getTheatre(@PathVariable Long id);

    @PostMapping("/api/theatres")
    Map<String, Object> createTheatre(@RequestBody TheatreRequest request);

    @PutMapping("/api/theatres/{id}")
    Map<String, Object> updateTheatre(@PathVariable Long id, @RequestBody TheatreRequest request);

    @DeleteMapping("/api/theatres/{id}")
    void deleteTheatre(@PathVariable Long id);
}
