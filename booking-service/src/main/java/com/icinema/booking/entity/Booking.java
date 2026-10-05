package com.icinema.booking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "booking")
public class Booking {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column (nullable=false, unique=true)
    private String bookingId;

    @Column (nullable=false)
    private String userEmail;

    @Column (nullable=false)
    private Long movieId;

    @Column (nullable=false)
    private Long theatreId;

    @Column (nullable=false)
    private Long screenId;

    @Column (nullable=false)
    private LocalDateTime showTime;

    @Column (nullable=false)
    private Double ticketPrice;

    @Column (nullable=true)
    private Double convenienceFee;

    @Column (nullable=false)
    private Double totalAmount;

    @Column (nullable=false)
    private String status;

    @Column (nullable=false)
    private LocalDateTime bookingTime;

    @Column (nullable=false)
    private LocalDateTime paymentExpiry;

    public Booking(){}

    public Long getId(){
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Double getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Double convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDateTime getPaymentExpiry() {
        return paymentExpiry;
    }

    public void setPaymentExpiry(LocalDateTime paymentExpiry) {
        this.paymentExpiry = paymentExpiry;
    }

    

}
