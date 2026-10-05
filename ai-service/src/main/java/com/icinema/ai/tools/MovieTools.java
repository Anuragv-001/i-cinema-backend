package com.icinema.ai.tools;

import com.icinema.ai.client.MovieClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MovieTools {

    private final MovieClient movieClient;

    public MovieTools(MovieClient movieClient) {
        this.movieClient = movieClient;
    }

    @Tool(description = "Get all movies currently available in the I-Cinema database. Use this tool to find a movie by its name, genre, or rating.")
    public List<Map<String, Object>> getMovies() {
        return movieClient.getMovies();
    }

    @Tool(description = "Get detailed information about a movie using its numeric database ID only. Do not pass a movie name to this tool. If the user provides a movie name, first call getMovies to find its ID.")
    public Map<String, Object> getMovieById(Long id) {
        return movieClient.getMovieById(id);
    }
}