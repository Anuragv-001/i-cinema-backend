package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "THEATRE-SERVICE", contextId = "screenClient")
public interface ScreenClient {

    @GetMapping("/api/screens")
    List<Map<String, Object>> getScreens();

    @GetMapping("/api/screens/{id}")
    Map<String, Object> getScreenById(@PathVariable Long id);

    @GetMapping("/api/screens/theatre/{theatreId}")
    List<Map<String, Object>> getScreensByTheatreId(
            @PathVariable Long theatreId
    );
}