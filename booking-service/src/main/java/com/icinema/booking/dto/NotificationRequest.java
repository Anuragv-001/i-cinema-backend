package com.icinema.booking.dto;

public class NotificationRequest {
    private String userEmail;
    private String bookingId;
    private String type;
    private String subject;
    private String message;

    public NotificationRequest(){}

    public NotificationRequest(
        String userEmail,
        String bookingId,
        String type,
        String subject,
        String message
    ){
        this.userEmail = userEmail;
        this.bookingId = bookingId;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
