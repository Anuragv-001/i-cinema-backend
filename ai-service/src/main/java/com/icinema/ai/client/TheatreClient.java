package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "THEATRE-SERVICE", contextId = "theatreClient")
public interface TheatreClient {

    @GetMapping("/api/theatres")
    List<Map<String, Object>> getTheatres();

    @GetMapping("/api/theatres/{id}")
    Map<String, Object> getTheatreById(@PathVariable Long id);
}