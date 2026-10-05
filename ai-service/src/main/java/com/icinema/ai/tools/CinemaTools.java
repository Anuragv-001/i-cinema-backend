package com.icinema.ai.tools;

import com.icinema.ai.client.MovieClient;
import com.icinema.ai.client.ScreenClient;
import com.icinema.ai.client.ShowClient;
import com.icinema.ai.client.TheatreClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CinemaTools {

    private final MovieClient movieClient;
    private final ShowClient showClient;
    private final TheatreClient theatreClient;
    private final ScreenClient screenClient;

    public CinemaTools(
            MovieClient movieClient,
            ShowClient showClient,
            TheatreClient theatreClient,
            ScreenClient screenClient) {

        this.movieClient = movieClient;
        this.showClient = showClient;
        this.theatreClient = theatreClient;
        this.screenClient = screenClient;
    }

    @Tool(description = """
        Find all cinema shows for a movie by its name.

        This is the ONLY tool that should be used when the user asks:
        - what shows are available for a movie
        - movie showtimes
        - movie timings
        - theatres showing a movie
        - screens showing a movie
        - ticket prices for a movie

        The input must be the movie name as plain text.
        Example: Hanuman Ansh

        Do NOT ask the user for a movie ID.
        Do NOT pass a movie ID to this tool.
        This tool automatically finds the movie ID from Movie Service.
        """)
    public List<Map<String, Object>> getMovieShows(String movieName) {

        if (movieName == null || movieName.trim().isEmpty()) {
            return List.of(Map.of(
                    "error",
                    "Movie name was not provided"
            ));
        }

        String searchName = movieName.trim();

        System.out.println("Searching movie: " + searchName);

        List<Map<String, Object>> movies = movieClient.getMovies();

        System.out.println("Movies from Movie Service: " + movies);

        Map<String, Object> movie = null;

        for (Map<String, Object> currentMovie : movies) {

            Object titleObject = currentMovie.get("title");

            if (titleObject == null) {
                continue;
            }

            String title = titleObject.toString().trim();

            if (title.equalsIgnoreCase(searchName)
                    || title.toLowerCase().contains(searchName.toLowerCase())
                    || searchName.toLowerCase().contains(title.toLowerCase())) {

                movie = currentMovie;
                break;
            }
        }

        if (movie == null) {
            return List.of(Map.of(
                    "error",
                    "Movie '" + searchName + "' was not found in Movie Service",
                    "availableMovies",
                    movies.stream()
                            .map(m -> String.valueOf(m.get("title")))
                            .toList()
            ));
        }

        Object movieIdObject = movie.get("id");

        if (movieIdObject == null) {
            movieIdObject = movie.get("movieId");
        }

        if (movieIdObject == null) {
            return List.of(Map.of(
                    "error",
                    "Movie was found but its ID is missing",
                    "movie",
                    movie
            ));
        }

        Long movieId;

        if (movieIdObject instanceof Number number) {
            movieId = number.longValue();
        } else {
            try {
                movieId = Long.parseLong(movieIdObject.toString());
            } catch (NumberFormatException e) {
                return List.of(Map.of(
                        "error",
                        "Invalid movie ID: " + movieIdObject
                ));
            }
        }

        System.out.println("Found movie: " + movie.get("title"));
        System.out.println("Movie ID: " + movieId);

        List<Map<String, Object>> shows =
                showClient.getShowsByMovie(movieId);

        System.out.println("Shows found: " + shows.size());
        System.out.println("Show data: " + shows);

        if (shows.isEmpty()) {
            return List.of(Map.of(
                    "message",
                    "No shows are available for " + movie.get("title"),
                    "movieId",
                    movieId
            ));
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> show : shows) {

            Object theatreIdObject = show.get("theatreId");
            Object screenIdObject = show.get("screenId");

            if (theatreIdObject == null || screenIdObject == null) {
                continue;
            }

            Long theatreId =
                    theatreIdObject instanceof Number number
                            ? number.longValue()
                            : Long.parseLong(theatreIdObject.toString());

            Long screenId =
                    screenIdObject instanceof Number number
                            ? number.longValue()
                            : Long.parseLong(screenIdObject.toString());

            Map<String, Object> theatre =
                    theatreClient.getTheatreById(theatreId);

            Map<String, Object> screen =
                    screenClient.getScreenById(screenId);

            Map<String, Object> showInfo = new HashMap<>();

            showInfo.put("movieTitle", movie.get("title"));
            showInfo.put("showId", show.get("id"));
            showInfo.put("showDate", show.get("showDate"));
            showInfo.put("startTime", show.get("startTime"));
            showInfo.put("endTime", show.get("endTime"));
            showInfo.put("ticketPrice", show.get("ticketPrice"));
            showInfo.put("theatre", theatre.get("name"));
            showInfo.put("theatreLocation", theatre.get("location"));
            showInfo.put("screenNumber", screen.get("screenNumber"));
            showInfo.put("screenType", screen.get("screenType"));
            showInfo.put("status", show.get("status"));

            result.add(showInfo);
        }

        return result;
    }
}