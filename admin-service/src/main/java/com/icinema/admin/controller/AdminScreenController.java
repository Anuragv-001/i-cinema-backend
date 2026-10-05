package com.icinema.admin.controller;

import com.icinema.admin.client.ScreenClient;
import com.icinema.admin.dto.ScreenRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/screens")
public class AdminScreenController {

    private final ScreenClient screenClient;

    public AdminScreenController(ScreenClient screenClient) {
        this.screenClient = screenClient;
    }

    @GetMapping
    public List<Map<String, Object>> getScreens() {
        return screenClient.getScreens();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getScreen(@PathVariable Long id) {
        return screenClient.getScreen(id);
    }

    @GetMapping("/theatre/{theatreId}")
    public List<Map<String, Object>> getScreensByTheatre(
            @PathVariable Long theatreId
    ) {
        return screenClient.getScreensByTheatre(theatreId);
    }

    @PostMapping
    public Map<String, Object> createScreen(
            @RequestBody ScreenRequest request
    ) {
        return screenClient.createScreen(request);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateScreen(
            @PathVariable Long id,
            @RequestBody ScreenRequest request
    ) {
        return screenClient.updateScreen(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteScreen(@PathVariable Long id) {
        screenClient.deleteScreen(id);
    }
}