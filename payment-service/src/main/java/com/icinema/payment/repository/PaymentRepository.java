package com.icinema.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.icinema.payment.entity.Payment;
import java.util.List;



public interface PaymentRepository extends JpaRepository<Payment, Long>{
    Optional<Payment> findByPaymentId(String paymentId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    List<Payment> findByUserEmail(String userEmail);

    Optional<Payment> findByBookingId(String bookingId);

}
