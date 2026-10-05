package com.icinema.admin.client;

import com.icinema.admin.dto.MovieRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MOVIE-SERVICE")
public interface MovieClient {
    @GetMapping("/api/movies")
    List<Map<String,Object>> getMovies();

    @GetMapping("/api/movies/{id}")
    Map<String,Object> getMovie(@PathVariable Long id);

    @PostMapping("/api/movies")
    Map<String,Object> createMovie(@RequestBody MovieRequest request);

    @PutMapping("/api/movies/{id}")
    Map<String,Object> updateMovie(@PathVariable Long id, @RequestBody MovieRequest request);

    @DeleteMapping("/api/movies/{id}")
    void deleteMovie(@PathVariable Long id);
}
