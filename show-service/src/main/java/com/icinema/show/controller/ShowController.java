package com.icinema.show.controller;

import com.icinema.show.entity.Show;
import com.icinema.show.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @PostMapping
    public ResponseEntity<Show> createShow(@RequestBody Show show) {
        return ResponseEntity.ok(showService.createShow(show));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(
            @PathVariable Long id,
            @RequestBody Show show) {
        return ResponseEntity.ok(showService.updateShow(id, show));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok("Show deleted successfully");
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Show>> getShowsByMovie(
            @PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Show>> getShowsByTheatre(
            @PathVariable Long theatreId) {
        return ResponseEntity.ok(showService.getShowsByTheatre(theatreId));
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<Show>> getShowsByScreen(
            @PathVariable Long screenId) {
        return ResponseEntity.ok(showService.getShowsByScreen(screenId));
    }

    @GetMapping("/movie/{movieId}/date/{date}")
    public ResponseEntity<List<Show>> getShowsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable LocalDate date) {
        return ResponseEntity.ok(
                showService.getShowsByMovieAndDate(movieId, date)
        );
    }

    @GetMapping("/screen/{screenId}/date/{date}")
    public ResponseEntity<List<Show>> getShowsByScreenAndDate(
            @PathVariable Long screenId,
            @PathVariable LocalDate date) {
        return ResponseEntity.ok(
                showService.getShowsByScreenAndDate(screenId, date)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<Show>> getActiveShows() {
        return ResponseEntity.ok(showService.getActiveShows());
    }
}