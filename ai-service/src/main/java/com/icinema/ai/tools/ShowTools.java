package com.icinema.ai.tools;

import com.icinema.ai.client.ShowClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class ShowTools {

    private final ShowClient showClient;

    public ShowTools(ShowClient showClient) {
        this.showClient = showClient;
    }

    @Tool(description = "Get all available cinema shows from I-Cinema. Use this when the user asks about showtimes or available shows.")
    public List<Map<String, Object>> getShows() {
        return showClient.getShows();
    }

    @Tool(description = "Get shows for a movie. movieId must be provided as a numeric string such as 1 or 2.")
    public List<Map<String, Object>> getShowsByMovie(String movieId) {
        return showClient.getShowsByMovie(Long.parseLong(movieId));
    }

    @Tool(description = "Get shows for a movie on a date. movieId must be a numeric string and date must use YYYY-MM-DD format.")
    public List<Map<String, Object>> getShowsByMovieAndDate(
            String movieId,
            String date
    ) {
        return showClient.getShowsByMovieAndDate(
                Long.parseLong(movieId),
                LocalDate.parse(date)
        );
    }
}