package com.icinema.admin.client;

import com.icinema.admin.dto.ShowRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "SHOW-SERVICE",
        contextId = "showClient"
)
public interface ShowClient {

    @GetMapping("/api/shows")
    List<Map<String, Object>> getShows();

    @GetMapping("/api/shows/{id}")
    Map<String, Object> getShow(@PathVariable Long id);

    @PostMapping("/api/shows")
    Map<String, Object> createShow(@RequestBody ShowRequest request);

    @PutMapping("/api/shows/{id}")
    Map<String, Object> updateShow(
            @PathVariable Long id,
            @RequestBody ShowRequest request
    );

    @DeleteMapping("/api/shows/{id}")
    String deleteShow(@PathVariable Long id);

    @GetMapping("/api/shows/movie/{movieId}")
    List<Map<String, Object>> getShowsByMovie(
            @PathVariable Long movieId
    );

    @GetMapping("/api/shows/theatre/{theatreId}")
    List<Map<String, Object>> getShowsByTheatre(
            @PathVariable Long theatreId
    );

    @GetMapping("/api/shows/screen/{screenId}")
    List<Map<String, Object>> getShowsByScreen(
            @PathVariable Long screenId
    );

    @GetMapping("/api/shows/movie/{movieId}/date/{date}")
    List<Map<String, Object>> getShowsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable String date
    );

    @GetMapping("/api/shows/screen/{screenId}/date/{date}")
    List<Map<String, Object>> getShowsByScreenAndDate(
            @PathVariable Long screenId,
            @PathVariable String date
    );

    @GetMapping("/api/shows/active")
    List<Map<String, Object>> getActiveShows();
}