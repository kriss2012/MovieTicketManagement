package com.movie.ticket.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.movie.ticket.entity.Booking;
import com.movie.ticket.service.BookingService;
import com.movie.ticket.service.MovieService;
import com.movie.ticket.service.ShowService;
import com.movie.ticket.service.TheatreService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;
    private final TheatreService theatreService;
    private final ShowService showService;
    private final BookingService bookingService;

    public AdminController(MovieService movieService,
                           TheatreService theatreService,
                           ShowService showService,
                           BookingService bookingService) {
        this.movieService = movieService;
        this.theatreService = theatreService;
        this.showService = showService;
        this.bookingService = bookingService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        int totalMovies = movieService.getAllMovies().size();
        int totalTheatres = theatreService.getAllTheatres().size();
        int totalShows = showService.getAllShows().size();

        List<Booking> bookings = bookingService.getAllBookings();
        int totalBookings = bookings.size();
        double totalRevenue = bookings.stream().mapToDouble(Booking::getTotalAmount).sum();

        model.addAttribute("totalMovies", totalMovies);
        model.addAttribute("totalTheatres", totalTheatres);
        model.addAttribute("totalShows", totalShows);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);

        model.addAttribute("recentBookings", bookings.stream().limit(5).toList());

        return "admin/dashboard";
    }
}
