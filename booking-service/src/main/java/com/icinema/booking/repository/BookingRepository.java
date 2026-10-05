package com.icinema.booking.repository;
import com.icinema.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);
    
    List<Booking> findByUserEmail(String userEmail);

    List<Booking> findByScreenIdAndShowTimeAndStatus(Long screenId, 
        LocalDateTime showTime, 
        String status);

    List<Booking> findByScreenIdAndShowTimeAndStatusIn(Long screenId, 
        LocalDateTime showTime, 
        List<String> statuses);

    List<Booking> findByStatusAndPaymentExpiryBefore(String status, LocalDateTime time);
}
