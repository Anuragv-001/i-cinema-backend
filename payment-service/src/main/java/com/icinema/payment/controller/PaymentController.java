package com.icinema.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.icinema.payment.dto.PaymentRequest;
import com.icinema.payment.dto.PaymentVerificationRequest;
import com.icinema.payment.entity.Payment;
import com.icinema.payment.service.PaymentService;
import com.razorpay.RazorpayException;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (RazorpayException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Razorpay error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {
        try {
            Payment payment = paymentService.verifyPayment(request);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentByPaymentId(
            @PathVariable String paymentId) {
        try {
            return ResponseEntity.ok(
                    paymentService.getPaymentByPaymentId(paymentId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(
            @PathVariable String bookingId) {
        try {
            return ResponseEntity.ok(
                    paymentService.getPaymentByBookingId(bookingId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Payment>> getPaymentsByUser(
            @RequestParam String email) {
        return ResponseEntity.ok(
                paymentService.getPaymentsByUser(email)
        );
    }

    @PutMapping("/refund/{paymentId}")
    public ResponseEntity<?> refundPayment(
            @PathVariable String paymentId) {
        try {
            return ResponseEntity.ok(
                    paymentService.refundPayment(paymentId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/refund/booking/{bookingId}")
    public ResponseEntity<?> refundBooking(
            @PathVariable String bookingId) {
        try {
            return ResponseEntity.ok(
                    paymentService.refundBooking(bookingId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}