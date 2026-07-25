package com.movie.ticket.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.ticket.entity.Booking;
import com.movie.ticket.entity.SeatBooking;
import com.movie.ticket.entity.Show;
import com.movie.ticket.entity.User;
import com.movie.ticket.repository.BookingRepository;
import com.movie.ticket.repository.SeatBookingRepository;
import com.movie.ticket.repository.ShowRepository;
import com.movie.ticket.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final SeatBookingRepository seatBookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                               ShowRepository showRepository,
                               SeatBookingRepository seatBookingRepository) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingDateDesc();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference).orElse(null);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserUserIdOrderByBookingDateDesc(userId);
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmailOrderByBookingDateDesc(email);
    }

    @Override
    public List<String> getBookedSeatsForShow(Long showId) {
        return seatBookingRepository.findBookedSeatsByShowId(showId);
    }

    @Override
    @Transactional
    public Booking processBooking(Long showId, User user, String customerName, String customerEmail,
                                   List<String> seatNumbers, String paymentMethod) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found with ID: " + showId));

        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new IllegalArgumentException("Please select at least one seat.");
        }

        // Verify seats are not already booked
        List<String> alreadyBooked = seatBookingRepository.findBookedSeatsByShowId(showId);
        for (String seat : seatNumbers) {
            if (alreadyBooked.contains(seat.trim())) {
                throw new IllegalArgumentException("Seat " + seat + " has already been booked. Please choose another seat.");
            }
        }

        if (show.getAvailableSeats() != null && show.getAvailableSeats() < seatNumbers.size()) {
            throw new IllegalArgumentException("Not enough available seats for this show.");
        }

        int seatCount = seatNumbers.size();
        double ticketPrice = show.getTicketPrice() != null ? show.getTicketPrice() : 200.0;
        double ticketAmount = ticketPrice * seatCount;
        double convenienceFee = 35.40 * seatCount; // BookMyShow standard fee per ticket
        double totalAmount = ticketAmount + convenienceFee;

        // Unique booking reference code
        String refCode = "BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking();
        booking.setShow(show);
        booking.setUser(user);
        booking.setCustomerName(customerName);
        booking.setCustomerEmail(customerEmail);
        booking.setNumberOfSeats(seatCount);
        booking.setSelectedSeats(String.join(", ", seatNumbers));
        booking.setTicketAmount(ticketAmount);
        booking.setConvenienceFee(convenienceFee);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingReference(refCode);
        booking.setPaymentMethod(paymentMethod != null ? paymentMethod : "UPI");
        booking.setPaymentStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        // Save individual SeatBookings
        for (String seat : seatNumbers) {
            SeatBooking seatBooking = new SeatBooking(show, savedBooking, seat.trim());
            seatBookingRepository.save(seatBooking);
        }

        // Deduct available seats in show
        if (show.getAvailableSeats() != null) {
            show.setAvailableSeats(show.getAvailableSeats() - seatCount);
            showRepository.save(show);
        }

        return savedBooking;
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}