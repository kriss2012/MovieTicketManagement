package com.movie.ticket.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.movie.ticket.entity.Booking;
import com.movie.ticket.entity.Show;
import com.movie.ticket.entity.User;
import com.movie.ticket.service.BookingService;
import com.movie.ticket.service.ShowService;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final ShowService showService;

    public BookingController(BookingService bookingService, ShowService showService) {
        this.bookingService = bookingService;
        this.showService = showService;
    }

    // Step 1: Seat Selection View for a specific Show
    @GetMapping("/shows/{showId}/seats")
    public String showSeatSelection(@PathVariable Long showId, Model model) {
        Show show = showService.getShowById(showId);
        if (show == null) {
            return "redirect:/movies";
        }

        List<String> bookedSeats = bookingService.getBookedSeatsForShow(showId);

        model.addAttribute("show", show);
        model.addAttribute("bookedSeats", bookedSeats);

        return "seat-layout";
    }

    // Step 2: Booking Summary & Payment Preparation Page
    @PostMapping("/bookings/summary")
    public String showBookingSummary(@RequestParam("showId") Long showId,
                                     @RequestParam("selectedSeats") String selectedSeats,
                                     HttpSession session,
                                     Model model) {

        Show show = showService.getShowById(showId);
        User user = (User) session.getAttribute("loggedInUser");

        if (show == null || selectedSeats == null || selectedSeats.trim().isEmpty()) {
            return "redirect:/movies";
        }

        List<String> seatList = Arrays.stream(selectedSeats.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        int seatCount = seatList.size();
        double ticketPrice = show.getTicketPrice() != null ? show.getTicketPrice() : 200.0;
        double ticketAmount = ticketPrice * seatCount;
        double convenienceFee = 35.40 * seatCount;
        double totalAmount = ticketAmount + convenienceFee;

        model.addAttribute("show", show);
        model.addAttribute("user", user);
        model.addAttribute("seatList", seatList);
        model.addAttribute("selectedSeatsStr", String.join(", ", seatList));
        model.addAttribute("seatCount", seatCount);
        model.addAttribute("ticketAmount", ticketAmount);
        model.addAttribute("convenienceFee", convenienceFee);
        model.addAttribute("totalAmount", totalAmount);

        return "booking/summary";
    }

    // Step 3: Process Booking & Confirm Payment
    @PostMapping("/bookings/confirm")
    public String processBookingConfirm(@RequestParam("showId") Long showId,
                                        @RequestParam("selectedSeats") String selectedSeatsStr,
                                        @RequestParam("customerName") String customerName,
                                        @RequestParam("customerEmail") String customerEmail,
                                        @RequestParam("paymentMethod") String paymentMethod,
                                        HttpSession session,
                                        Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        List<String> seatList = Arrays.stream(selectedSeatsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        try {
            Booking booking = bookingService.processBooking(
                    showId, user, customerName, customerEmail, seatList, paymentMethod
            );
            return "redirect:/bookings/ticket/" + booking.getBookingReference();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return showSeatSelection(showId, model);
        }
    }

    // Step 4: Digital Ticket View
    @GetMapping("/bookings/ticket/{reference}")
    public String viewTicket(@PathVariable String reference, Model model) {
        Booking booking = bookingService.getBookingByReference(reference);
        if (booking == null) {
            return "redirect:/";
        }
        model.addAttribute("booking", booking);
        return "booking/ticket";
    }

    // Step 5: User Booking History ("My Bookings")
    @GetMapping("/my-bookings")
    public String viewMyBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingService.getBookingsByUserId(user.getUserId());
        if (bookings.isEmpty()) {
            bookings = bookingService.getBookingsByEmail(user.getEmail());
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);

        return "booking/my-bookings";
    }

    // Legacy / All Bookings Table
    @GetMapping("/bookings")
    public String viewAllBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null && user.isAdmin()) {
            model.addAttribute("bookings", bookingService.getAllBookings());
            return "bookings";
        } else if (user != null) {
            return "redirect:/my-bookings";
        }
        return "redirect:/login";
    }
}
