package com.movie.ticket.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.movie.ticket.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserIdOrderByBookingDateDesc(Long userId);
    List<Booking> findByCustomerEmailOrderByBookingDateDesc(String email);
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findAllByOrderByBookingDateDesc();
}