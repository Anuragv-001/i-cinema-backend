package com.icinema.admin.controller;

import com.icinema.admin.client.TheatreClient;
import com.icinema.admin.dto.TheatreRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/theatres")
public class AdminTheatreController {

    private final TheatreClient theatreClient;

    public AdminTheatreController(TheatreClient theatreClient) {
        this.theatreClient = theatreClient;
    }

    @GetMapping
    public List<Map<String, Object>> getTheatres() {
        return theatreClient.getTheatres();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getTheatre(@PathVariable Long id) {
        return theatreClient.getTheatre(id);
    }

    @PostMapping
    public Map<String, Object> createTheatre(
            @RequestBody TheatreRequest request
    ) {
        return theatreClient.createTheatre(request);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateTheatre(
            @PathVariable Long id,
            @RequestBody TheatreRequest request
    ) {
        return theatreClient.updateTheatre(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTheatre(@PathVariable Long id) {
        theatreClient.deleteTheatre(id);
    }
}