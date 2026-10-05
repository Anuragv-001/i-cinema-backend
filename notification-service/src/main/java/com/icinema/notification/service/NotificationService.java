package com.icinema.notification.service;

import com.icinema.notification.dto.NotificationRequest;
import com.icinema.notification.entity.Notification;
import com.icinema.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationService(
            NotificationRepository notificationRepository,
            EmailService emailService
    ) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Notification sendNotification(NotificationRequest request) {

        Notification notification = new Notification();

        notification.setNotificationId(
                "NOT-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        notification.setUserEmail(request.getUserEmail());
        notification.setBookingId(request.getBookingId());
        notification.setType(request.getType());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus("PENDING");

        Notification saved = notificationRepository.save(notification);

        try {
            emailService.sendEmail(
                    request.getUserEmail(),
                    request.getSubject(),
                    request.getMessage()
            );

            saved.setStatus("SENT");

        } catch (Exception e) {

            saved.setStatus("FAILED");
        }

        return notificationRepository.save(saved);
    }

    public List<Notification> getUserNotifications(String email) {
        return notificationRepository.findByUserEmail(email);
    }

    public List<Notification> getBookingNotifications(String bookingId) {
        return notificationRepository.findByBookingId(bookingId);
    }
}