package com.icinema.admin.controller;

import com.icinema.admin.client.MovieClient;
import com.icinema.admin.dto.MovieRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/movies")
public class AdminMovieController {

    private final MovieClient movieClient;

    public AdminMovieController(MovieClient movieClient) {
        this.movieClient = movieClient;
    }

    @GetMapping
    public List<Map<String, Object>> getMovies() {
        return movieClient.getMovies();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getMovie(@PathVariable Long id) {
        return movieClient.getMovie(id);
    }

    @PostMapping
    public Map<String, Object> createMovie(
            @RequestBody MovieRequest request
    ) {
        return movieClient.createMovie(request);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieRequest request
    ) {
        return movieClient.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieClient.deleteMovie(id);
    }
}