package com.icinema.ai.controller;

import com.icinema.ai.tools.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final ChatClient chatClient;
    private final CinemaTools cinemaTools;
    private final MovieTools movieTools;
    private final ShowTools showTools;
    private final TheatreTools theatreTools;
    private final SeatTools seatTools;

    public AIController(
            ChatClient.Builder chatClientBuilder,
            CinemaTools cinemaTools,
            MovieTools movieTools,
            ShowTools showTools,
            TheatreTools theatreTools, SeatTools seatTools) {

        this.chatClient = chatClientBuilder.build();
        this.cinemaTools = cinemaTools;
        this.movieTools = movieTools;
        this.showTools = showTools;
        this.theatreTools = theatreTools;
        this.seatTools = seatTools;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {

        return chatClient
                .prompt()
                .system("""
                    You are the I-Cinema cinema assistant.

                    You have access to real I-Cinema backend data.

                    IMPORTANT RULES:

                    1. Never invent movie, show, theatre, screen or price information.

                    2. When the user asks about shows, showtimes, timings,
                       theatres, screens or ticket prices for a movie,
                       ALWAYS use CinemaTools.getMovieShows().

                    3. CinemaTools.getMovieShows() requires the MOVIE NAME,
                       not the movie ID.

                    4. For example, if the user asks:
                       "What shows are available for Hanuman Ansh?"
                       call:
                       getMovieShows("Hanuman Ansh")

                    5. Do NOT call ShowTools.getShowsByMovie() yourself
                       for movie show queries.

                    6. Do NOT invent or guess movie IDs.

                    7. Do NOT ask the user for a movie ID when the movie name
                       is provided.

                    8. CinemaTools returns the actual show information from
                       the Movie, Show and Theatre services.

                    9. If CinemaTools returns multiple shows, report all
                       available shows.

                    10. If CinemaTools returns an error, clearly explain the
                        returned error instead of inventing an answer.

                    11. When showing show information, include:
                        movie title, date, start time, end time, theatre,
                        screen and ticket price when available.

                    12. Treat backend tool results as the source of truth.
                    
                    13. Do not add generic disclaimers such as "prices may change",
                        "timings are subject to change", or similar statements unless
                        the backend data explicitly provides such information.
                    14. When the user asks about seat availability for a specific show,
                         first use CinemaTools.getMovieShows() to identify the correct
                         show date, start time and screen ID.
                        
                    15. Then use SeatTools.getSeatAvailability() with the actual screen ID,
                         show date and start time returned by the backend.
                        
                    16. Never invent seat availability or seat numbers.
                        
                    17. Only report a seat as occupied when it is returned by the
                        Booking Service occupied-seats endpoint.
                    """)
                .user(request.message())
                .tools(cinemaTools, movieTools, showTools, theatreTools, seatTools)
                .call()
                .content();
    }

    public record ChatRequest(String message) {}
}