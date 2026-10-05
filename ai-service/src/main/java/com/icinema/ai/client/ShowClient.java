package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "SHOW-SERVICE")
public interface ShowClient {

    @GetMapping("/api/shows")
    List<Map<String, Object>> getShows();

    @GetMapping("/api/shows/movie/{movieId}")
    List<Map<String, Object>> getShowsByMovie(
            @PathVariable Long movieId
    );

    @GetMapping("/api/shows/movie/{movieId}/date/{date}")
    List<Map<String, Object>> getShowsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable LocalDate date
    );
}