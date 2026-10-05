package com.icinema.notification.controller;

import com.icinema.notification.dto.NotificationRequest;
import com.icinema.notification.entity.Notification;
import com.icinema.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(
            @RequestBody NotificationRequest request
    ) {
        return ResponseEntity.ok(
                notificationService.sendNotification(request)
        );
    }

    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(email)
        );
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Notification>> getBookingNotifications(
            @PathVariable String bookingId
    ) {
        return ResponseEntity.ok(
                notificationService.getBookingNotifications(bookingId)
        );
    }
}