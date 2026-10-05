package com.icinema.ai.tools;

import com.icinema.ai.client.ScreenClient;
import com.icinema.ai.client.TheatreClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TheatreTools {

    private final TheatreClient theatreClient;
    private final ScreenClient screenClient;

    public TheatreTools(
            TheatreClient theatreClient,
            ScreenClient screenClient) {
        this.theatreClient = theatreClient;
        this.screenClient = screenClient;
    }

    @Tool(description = """
        Get all theatres available in I-Cinema.
        Use this when you need to identify a theatre from a theatre ID.
        """)
    public List<Map<String, Object>> getTheatres() {
        return theatreClient.getTheatres();
    }

    @Tool(description = """
        Get detailed information about a theatre using its numeric theatre ID.
        """)
    public Map<String, Object> getTheatreById(String theatreId) {
        return theatreClient.getTheatreById(Long.parseLong(theatreId));
    }

    @Tool(description = """
        Get all cinema screens.
        Use this when you need to identify a screen from a screen ID.
        """)
    public List<Map<String, Object>> getScreens() {
        return screenClient.getScreens();
    }

    @Tool(description = """
        Get detailed information about a cinema screen using its numeric screen ID.
        """)
    public Map<String, Object> getScreenById(String screenId) {
        return screenClient.getScreenById(Long.parseLong(screenId));
    }

    @Tool(description = """
        Get all screens belonging to a specific theatre.
        theatreId must be a numeric string.
        """)
    public List<Map<String, Object>> getScreensByTheatreId(String theatreId) {
        return screenClient.getScreensByTheatreId(
                Long.parseLong(theatreId)
        );
    }
}