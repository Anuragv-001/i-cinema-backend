package com.icinema.gateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration 
public class GatewayConfig {
    @Bean 
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("movie-service", r -> r.path("/api/movies/**")
                        .uri("lb://MOVIE-SERVICE"))
                .route("theatre-service", r -> r.path("/api/theatres/**")
                        .uri("lb://THEATRE-SERVICE"))
                .route("screen-service", r -> r.path("/api/screens/**")
                        .uri("lb://THEATRE-SERVICE"))
                .route("seating-service", r -> r.path("/api/seats/**")
                        .uri("lb://SEATING-SERVICE"))
                .route("booking-service", r -> r.path("/api/bookings/**")
                        .uri("lb://BOOKING-SERVICE"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .uri("lb://PAYMENT-SERVICE"))
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://NOTIFICATION-SERVICE"))
                .route("show-service", r -> r.path("/api/shows/**")
                        .uri("lb://SHOW-SERVICE"))
                .route("admin-service", r -> r.path("/api/admin/**")
                        .uri("lb://ADMIN-SERVICE"))
                .route("ai-service", r -> r
                        .path("/api/ai/**")
                        .uri("lb://AI-SERVICE"))
                .build();
    }
}
