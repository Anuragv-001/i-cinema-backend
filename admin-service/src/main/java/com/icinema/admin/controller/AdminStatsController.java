package com.icinema.admin.controller;

import com.icinema.admin.client.BookingClient;
import com.icinema.admin.client.MovieClient;
import com.icinema.admin.client.PaymentClient;
import com.icinema.admin.client.ScreenClient;
import com.icinema.admin.client.ShowClient;
import com.icinema.admin.client.TheatreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final MovieClient movieClient;
    private final TheatreClient theatreClient;
    private final ScreenClient screenClient;
    private final ShowClient showClient;
    private final BookingClient bookingClient;
    private final PaymentClient paymentClient;

    public AdminStatsController(
            MovieClient movieClient,
            TheatreClient theatreClient,
            ScreenClient screenClient,
            ShowClient showClient,
            BookingClient bookingClient,
            PaymentClient paymentClient) {

        this.movieClient = movieClient;
        this.theatreClient = theatreClient;
        this.screenClient = screenClient;
        this.showClient = showClient;
        this.bookingClient = bookingClient;
        this.paymentClient = paymentClient;
    }

    @GetMapping("/overview")
    public Map<String, Object> getOverviewStats() {

        List<Map<String, Object>> movies = movieClient.getMovies();
        List<Map<String, Object>> theatres = theatreClient.getTheatres();
        List<Map<String, Object>> screens = screenClient.getScreens();
        List<Map<String, Object>> shows = showClient.getShows();
        List<Map<String, Object>> bookings = bookingClient.getBookings();
        List<Map<String, Object>> payments = paymentClient.getPayments();

        long confirmedBookings = bookings.stream()
                .filter(booking ->
                        "CONFIRMED".equals(
                                String.valueOf(booking.get("status"))
                        )
                )
                .count();

        long cancelledBookings = bookings.stream()
                .filter(booking ->
                        "CANCELLED".equals(
                                String.valueOf(booking.get("status"))
                        )
                )
                .count();

        double totalRevenue = payments.stream()
                .filter(payment ->
                        "SUCCESS".equals(
                                String.valueOf(payment.get("status"))
                        )
                )
                .mapToDouble(payment -> {
                    Object amount = payment.get("amount");

                    if (amount instanceof Number) {
                        return ((Number) amount).doubleValue();
                    }

                    try {
                        return Double.parseDouble(String.valueOf(amount));
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .sum();

        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalMovies", movies.size());
        stats.put("totalTheatres", theatres.size());
        stats.put("totalScreens", screens.size());
        stats.put("totalShows", shows.size());
        stats.put("totalBookings", bookings.size());
        stats.put("confirmedBookings", confirmedBookings);
        stats.put("cancelledBookings", cancelledBookings);
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }
}