package com.movie.ticket.service;

import java.util.List;
import com.movie.ticket.entity.Booking;
import com.movie.ticket.entity.User;

public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    Booking getBookingByReference(String reference);

    List<Booking> getBookingsByUserId(Long userId);

    List<Booking> getBookingsByEmail(String email);

    List<String> getBookedSeatsForShow(Long showId);

    Booking processBooking(Long showId, User user, String customerName, String customerEmail,
                           List<String> seatNumbers, String paymentMethod);

    Booking saveBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBooking(Long id);
}