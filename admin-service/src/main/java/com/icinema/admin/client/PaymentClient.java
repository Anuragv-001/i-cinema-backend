package com.icinema.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "PAYMENT-SERVICE",
        contextId = "paymentClient"
)
public interface PaymentClient {

    @GetMapping("/api/payments")
    List<Map<String, Object>> getPayments();

    @GetMapping("/api/payments/{paymentId}")
    Map<String, Object> getPayment(
            @PathVariable String paymentId
    );

    @GetMapping("/api/payments/booking/{bookingId}")
    Map<String, Object> getPaymentByBookingId(
            @PathVariable String bookingId
    );

    @GetMapping("/api/payments/user")
    List<Map<String, Object>> getPaymentsByUser(
            @RequestParam String email
    );

    @PutMapping("/api/payments/refund/{paymentId}")
    Map<String, Object> refundPayment(
            @PathVariable String paymentId
    );

    @PutMapping("/api/payments/refund/booking/{bookingId}")
    Map<String, Object> refundBooking(
            @PathVariable String bookingId
    );
}