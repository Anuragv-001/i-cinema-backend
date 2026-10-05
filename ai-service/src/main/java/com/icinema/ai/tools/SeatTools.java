package com.icinema.ai.tools;

import com.icinema.ai.client.BookingClient;
import com.icinema.ai.client.MovieClient;
import com.icinema.ai.client.SeatClient;
import com.icinema.ai.client.ShowClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SeatTools {

    private final MovieClient movieClient;
    private final ShowClient showClient;
    private final SeatClient seatClient;
    private final BookingClient bookingClient;

    public SeatTools(
            MovieClient movieClient,
            ShowClient showClient,
            SeatClient seatClient,
            BookingClient bookingClient) {

        this.movieClient = movieClient;
        this.showClient = showClient;
        this.seatClient = seatClient;
        this.bookingClient = bookingClient;
    }

    @Tool(description = """
        Check real seat availability for a movie show.

        Input:
        movieName = movie name
        showDate = date in YYYY-MM-DD format
        startTime = time in HH:mm or HH:mm:ss format

        Example:
        getSeatAvailability("Hanuman Ansh", "2026-09-28", "09:30")

        Do not ask the user for movie ID or screen ID.
        The tool finds the movie, show and screen automatically.
        """)
    public List<Map<String, Object>> getSeatAvailability(
            String movieName,
            String showDate,
            String startTime) {

        if (movieName == null || movieName.trim().isEmpty()) {
            return List.of(Map.of(
                    "error",
                    "Movie name was not provided"
            ));
        }

        if (showDate == null || showDate.trim().isEmpty()) {
            return List.of(Map.of(
                    "error",
                    "Show date was not provided"
            ));
        }

        if (startTime == null || startTime.trim().isEmpty()) {
            return List.of(Map.of(
                    "error",
                    "Show time was not provided"
            ));
        }

        LocalDate date;

        try {
            date = LocalDate.parse(showDate.trim());
        } catch (Exception e) {
            return List.of(Map.of(
                    "error",
                    "Invalid date: " + showDate + ". Expected YYYY-MM-DD."
            ));
        }

        LocalTime requestedTime;

        try {
            requestedTime = parseTime(startTime);
        } catch (Exception e) {
            return List.of(Map.of(
                    "error",
                    "Invalid time: " + startTime
            ));
        }

        System.out.println("========================================");
        System.out.println("SEAT AVAILABILITY CHECK");
        System.out.println("MOVIE NAME: " + movieName);
        System.out.println("DATE: " + date);
        System.out.println("TIME: " + requestedTime);
        System.out.println("========================================");

        List<Map<String, Object>> movies = movieClient.getMovies();

        Map<String, Object> movie = null;

        for (Map<String, Object> currentMovie : movies) {

            Object titleObject = currentMovie.get("title");

            if (titleObject == null) {
                continue;
            }

            String title = titleObject.toString().trim();

            String requestedMovie = movieName.trim().toLowerCase();

            if (title.equalsIgnoreCase(movieName.trim())
                    || title.toLowerCase().contains(requestedMovie)
                    || requestedMovie.contains(title.toLowerCase())) {

                movie = currentMovie;
                break;
            }
        }

        if (movie == null) {
            return List.of(Map.of(
                    "error",
                    "Movie not found: " + movieName
            ));
        }

        Object movieIdObject = movie.get("id");

        if (movieIdObject == null) {
            movieIdObject = movie.get("movieId");
        }

        if (movieIdObject == null) {
            return List.of(Map.of(
                    "error",
                    "Movie ID not found for: " + movieName
            ));
        }

        Long movieId;

        try {
            movieId = movieIdObject instanceof Number number
                    ? number.longValue()
                    : Long.parseLong(movieIdObject.toString());
        } catch (Exception e) {
            return List.of(Map.of(
                    "error",
                    "Invalid movie ID: " + movieIdObject
            ));
        }

        System.out.println("SEAT CHECK MOVIE: " + movie.get("title"));
        System.out.println("SEAT CHECK MOVIE ID: " + movieId);
        System.out.println("SEAT CHECK DATE: " + date);
        System.out.println("SEAT CHECK TIME: " + requestedTime);

        List<Map<String, Object>> shows;

        try {
            shows = showClient.getShowsByMovie(movieId);
        } catch (Exception e) {
            e.printStackTrace();

            return List.of(Map.of(
                    "error",
                    "Unable to retrieve shows for " + movie.get("title"),
                    "details",
                    e.getMessage() == null ? "Unknown error" : e.getMessage()
            ));
        }

        System.out.println("TOTAL SHOWS FOR MOVIE: " + shows.size());
        System.out.println("ALL SHOWS: " + shows);

        List<Map<String, Object>> matchingShows = new ArrayList<>();

        for (Map<String, Object> show : shows) {

            Object showDateObject = show.get("showDate");
            Object startTimeObject = show.get("startTime");

            if (showDateObject == null || startTimeObject == null) {
                continue;
            }

            String backendDate = showDateObject.toString().trim();
            String backendTime = startTimeObject.toString().trim();

            LocalDate showDateValue;
            LocalTime showTimeValue;

            try {
                showDateValue = LocalDate.parse(backendDate);
            } catch (Exception e) {
                System.out.println(
                        "Unable to parse show date: " + backendDate
                );
                continue;
            }

            try {
                showTimeValue = parseTime(backendTime);
            } catch (Exception e) {
                System.out.println(
                        "Unable to parse show time: " + backendTime
                );
                continue;
            }

            System.out.println(
                    "CHECKING SHOW -> DATE: "
                            + showDateValue
                            + " TIME: "
                            + showTimeValue
            );

            if (showDateValue.equals(date)
                    && showTimeValue.getHour() == requestedTime.getHour()
                    && showTimeValue.getMinute() == requestedTime.getMinute()) {

                matchingShows.add(show);
            }
        }

        System.out.println(
                "MATCHING SHOWS: " + matchingShows.size()
        );

        if (matchingShows.isEmpty()) {
            return List.of(Map.of(
                    "error",
                    "No show found for "
                            + movie.get("title")
                            + " on "
                            + date
                            + " at "
                            + requestedTime,
                    "movieId",
                    movieId,
                    "availableShows",
                    shows
            ));
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> show : matchingShows) {

            Object screenIdObject = show.get("screenId");

            if (screenIdObject == null) {
                continue;
            }

            Long screenId;

            try {
                screenId = screenIdObject instanceof Number number
                        ? number.longValue()
                        : Long.parseLong(screenIdObject.toString());
            } catch (Exception e) {
                continue;
            }

            LocalDateTime showTime =
                    LocalDateTime.of(date, requestedTime);

            System.out.println("----------------------------------------");
            System.out.println("PROCESSING SHOW");
            System.out.println("SHOW ID: " + show.get("id"));
            System.out.println("SCREEN ID: " + screenId);
            System.out.println("SHOW TIME: " + showTime);

            List<Map<String, Object>> seats;

            try {
                seats = seatClient.getSeatsByScreenId(screenId);
            } catch (Exception e) {
                e.printStackTrace();

                Map<String, Object> error = new HashMap<>();
                error.put("error", "Unable to retrieve seats");
                error.put("screenId", screenId);
                error.put("details",
                        e.getMessage() == null
                                ? "Unknown error"
                                : e.getMessage());

                result.add(error);
                continue;
            }

            List<Long> occupiedSeatIds;

            try {
                occupiedSeatIds =
                        bookingClient.getOccupiedSeatIds(
                                screenId,
                                showTime
                        );
            } catch (Exception e) {
                e.printStackTrace();

                occupiedSeatIds = new ArrayList<>();
            }

            if (occupiedSeatIds == null) {
                occupiedSeatIds = new ArrayList<>();
            }

            System.out.println(
                    "TOTAL SEATS: " + seats.size()
            );

            System.out.println(
                    "OCCUPIED SEAT IDS: " + occupiedSeatIds
            );

            List<Map<String, Object>> availableSeats =
                    new ArrayList<>();

            List<Map<String, Object>> occupiedSeats =
                    new ArrayList<>();

            for (Map<String, Object> seat : seats) {

                Object seatIdObject = seat.get("id");

                if (seatIdObject == null) {
                    continue;
                }

                Long seatId;

                try {
                    seatId = seatIdObject instanceof Number number
                            ? number.longValue()
                            : Long.parseLong(seatIdObject.toString());
                } catch (Exception e) {
                    continue;
                }

                if (occupiedSeatIds.contains(seatId)) {
                    occupiedSeats.add(seat);
                } else {
                    availableSeats.add(seat);
                }
            }

            System.out.println(
                    "AVAILABLE SEATS: " + availableSeats.size()
            );

            System.out.println(
                    "OCCUPIED SEATS: " + occupiedSeats.size()
            );

            Map<String, Object> availability =
                    new HashMap<>();

            availability.put(
                    "movieTitle",
                    movie.get("title")
            );

            availability.put(
                    "showId",
                    show.get("id")
            );

            availability.put(
                    "showDate",
                    show.get("showDate")
            );

            availability.put(
                    "startTime",
                    show.get("startTime")
            );

            availability.put(
                    "endTime",
                    show.get("endTime")
            );

            availability.put(
                    "screenId",
                    screenId
            );

            availability.put(
                    "totalSeats",
                    seats.size()
            );

            availability.put(
                    "occupiedCount",
                    occupiedSeats.size()
            );

            availability.put(
                    "availableCount",
                    availableSeats.size()
            );

            availability.put(
                    "availableSeats",
                    availableSeats
            );

            availability.put(
                    "occupiedSeats",
                    occupiedSeats
            );

            result.add(availability);
        }

        return result;
    }

    private LocalTime parseTime(String time) {

        String value = time.trim().toUpperCase();

        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ofPattern("H:mm"),
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("H:mm:ss"),
                DateTimeFormatter.ofPattern("HH:mm:ss"),
                DateTimeFormatter.ofPattern("h:mm a"),
                DateTimeFormatter.ofPattern("hh:mm a")
        );

        for (DateTimeFormatter formatter : formats) {

            try {
                return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException(
                "Unsupported time format: " + time
        );
    }
}