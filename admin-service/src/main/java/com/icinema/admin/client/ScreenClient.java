package com.icinema.admin.client;

import com.icinema.admin.dto.ScreenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "THEATRE-SERVICE", contextId = "screenClient")
public interface ScreenClient {

    @GetMapping("/api/screens")
    List<Map<String, Object>> getScreens();

    @GetMapping("/api/screens/{id}")
    Map<String, Object> getScreen(@PathVariable Long id);

    @GetMapping("/api/screens/theatre/{theatreId}")
    List<Map<String, Object>> getScreensByTheatre(
            @PathVariable Long theatreId
    );

    @PostMapping("/api/screens")
    Map<String, Object> createScreen(
            @RequestBody ScreenRequest request
    );

    @PutMapping("/api/screens/{id}")
    Map<String, Object> updateScreen(
            @PathVariable Long id,
            @RequestBody ScreenRequest request
    );

    @DeleteMapping("/api/screens/{id}")
    void deleteScreen(@PathVariable Long id);
}