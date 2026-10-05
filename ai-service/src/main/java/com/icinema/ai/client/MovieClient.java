package com.icinema.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MOVIE-SERVICE")
public interface MovieClient {

    @GetMapping("/api/movies")
    List<Map<String, Object>> getMovies();

    @GetMapping("/api/movies/{id}")
    Map<String, Object> getMovieById(@PathVariable Long id);
}