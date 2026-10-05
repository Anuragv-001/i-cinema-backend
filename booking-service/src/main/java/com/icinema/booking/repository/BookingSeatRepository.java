package com.icinema.booking.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icinema.booking.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBookingId(String bookingId);

    boolean existsBySeatId(Long seatId);

    List<BookingSeat> findBySeatId(Long seatId);
}
