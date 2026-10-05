package com.icinema.admin.controller;

import com.icinema.admin.client.ShowClient;
import com.icinema.admin.dto.ShowRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/shows")
public class AdminShowController {

    private final ShowClient showClient;

    public AdminShowController(ShowClient showClient) {
        this.showClient = showClient;
    }

    @GetMapping
    public List<Map<String, Object>> getShows() {
        return showClient.getShows();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getShow(@PathVariable Long id) {
        return showClient.getShow(id);
    }

    @PostMapping
    public Map<String, Object> createShow(
            @RequestBody ShowRequest request) {
        return showClient.createShow(request);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateShow(
            @PathVariable Long id,
            @RequestBody ShowRequest request) {
        return showClient.updateShow(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteShow(@PathVariable Long id) {
        return showClient.deleteShow(id);
    }

    @GetMapping("/movie/{movieId}")
    public List<Map<String, Object>> getShowsByMovie(
            @PathVariable Long movieId) {
        return showClient.getShowsByMovie(movieId);
    }

    @GetMapping("/theatre/{theatreId}")
    public List<Map<String, Object>> getShowsByTheatre(
            @PathVariable Long theatreId) {
        return showClient.getShowsByTheatre(theatreId);
    }

    @GetMapping("/screen/{screenId}")
    public List<Map<String, Object>> getShowsByScreen(
            @PathVariable Long screenId) {
        return showClient.getShowsByScreen(screenId);
    }

    @GetMapping("/movie/{movieId}/date/{date}")
    public List<Map<String, Object>> getShowsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable String date) {
        return showClient.getShowsByMovieAndDate(movieId, date);
    }

    @GetMapping("/screen/{screenId}/date/{date}")
    public List<Map<String, Object>> getShowsByScreenAndDate(
            @PathVariable Long screenId,
            @PathVariable String date) {
        return showClient.getShowsByScreenAndDate(screenId, date);
    }

    @GetMapping("/active")
    public List<Map<String, Object>> getActiveShows() {
        return showClient.getActiveShows();
    }
}