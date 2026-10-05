package com.icinema.payment.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.icinema.payment.dto.PaymentRequest;
import com.icinema.payment.entity.Payment;
import com.icinema.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.icinema.payment.dto.PaymentVerificationRequest;
import com.razorpay.Utils;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    public PaymentService(
            PaymentRepository paymentRepository,
            RazorpayClient razorpayClient) {
        this.paymentRepository = paymentRepository;
        this.razorpayClient = razorpayClient;
    }

    public Payment createOrder(PaymentRequest request) throws RazorpayException {

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (request.getBookingId() == null || request.getBookingId().isBlank()) {
            throw new RuntimeException("Booking ID is required");
        }

        JSONObject orderRequest = new JSONObject();

        long amountInPaise = Math.round(request.getAmount() * 100);

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", request.getCurrency() == null ? "INR" : request.getCurrency());
        orderRequest.put("receipt", request.getBookingId());

        Order order = razorpayClient.orders.create(orderRequest);

        Payment payment = new Payment();

        payment.setPaymentId(
                "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );

        payment.setBookingId(request.getBookingId());
        payment.setUserEmail(request.getUserEmail());
        payment.setAmount(request.getAmount());
        payment.setCurrency(
                request.getCurrency() == null ? "INR" : request.getCurrency()
        );
        payment.setPaymentMethod("RAZORPAY");
        payment.setStatus("CREATED");
        payment.setGatewayOrderId(order.get("id"));
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment verifyPayment(PaymentVerificationRequest request) throws Exception {

    JSONObject attributes = new JSONObject();

    attributes.put("razorpay_order_id", request.getRazorpayOrderId());
    attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
    attributes.put("razorpay_signature", request.getRazorpaySignature());

    boolean verified = Utils.verifyPaymentSignature(
            attributes,
            System.getenv("RAZORPAY_KEY_SECRET")
    );

    if (!verified) {
        throw new RuntimeException("Payment signature verification failed");
    }

    Payment payment = paymentRepository
            .findByGatewayOrderId(request.getRazorpayOrderId())
            .orElseThrow(() -> new RuntimeException("Payment order not found"));

    payment.setGatewayPaymentId(request.getRazorpayPaymentId());
    payment.setStatus("SUCCESS");
    payment.setUpdatedAt(LocalDateTime.now());

    return paymentRepository.save(payment);
}

    public Payment refundPayment(String paymentId) {
    try {
        Payment payment = paymentRepository.findByGatewayPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new RuntimeException("Payment is not eligible for refund");
        }

        JSONObject refundRequest = new JSONObject();
        refundRequest.put("amount", (long) (payment.getAmount() * 100));

        com.razorpay.Refund refund = razorpayClient.payments.refund(
                payment.getGatewayPaymentId(),
                refundRequest
        );

        payment.setStatus("REFUNDED");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);

    } catch (RazorpayException e) {
        throw new RuntimeException("Refund failed: " + e.getMessage());
    }
}
    public Payment refundBooking(String bookingId) {
    try {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking"));

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new RuntimeException(
                    "Payment is not eligible for refund. Current status: "
                            + payment.getStatus()
            );
        }

        JSONObject refundRequest = new JSONObject();
        refundRequest.put("amount", (long) (payment.getAmount() * 100));

        razorpayClient.payments.refund(
                payment.getGatewayPaymentId(),
                refundRequest
        );

        payment.setStatus("REFUNDED");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);

    } catch (RazorpayException e) {
        throw new RuntimeException("Refund failed: " + e.getMessage());
    }
}

    public java.util.List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment getPaymentByBookingId(String bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking"));
    }

    public java.util.List<Payment> getPaymentsByUser(String userEmail) {
        return paymentRepository.findByUserEmail(userEmail);
    }
}