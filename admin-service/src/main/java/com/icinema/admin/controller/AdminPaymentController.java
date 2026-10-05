package com.icinema.admin.controller;

import com.icinema.admin.client.PaymentClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final PaymentClient paymentClient;

    public AdminPaymentController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @GetMapping
    public List<Map<String, Object>> getPayments() {
        return paymentClient.getPayments();
    }

    @GetMapping("/{paymentId}")
    public Map<String, Object> getPayment(
            @PathVariable String paymentId) {
        return paymentClient.getPayment(paymentId);
    }

    @GetMapping("/booking/{bookingId}")
    public Map<String, Object> getPaymentByBookingId(
            @PathVariable String bookingId) {
        return paymentClient.getPaymentByBookingId(bookingId);
    }

    @GetMapping("/user")
    public List<Map<String, Object>> getPaymentsByUser(
            @RequestParam String email) {
        return paymentClient.getPaymentsByUser(email);
    }

    @PutMapping("/refund/{paymentId}")
    public Map<String, Object> refundPayment(
            @PathVariable String paymentId) {
        return paymentClient.refundPayment(paymentId);
    }

    @PutMapping("/refund/booking/{bookingId}")
    public Map<String, Object> refundBooking(
            @PathVariable String bookingId) {
        return paymentClient.refundBooking(bookingId);
    }
}