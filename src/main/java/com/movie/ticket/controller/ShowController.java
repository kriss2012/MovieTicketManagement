package com.movie.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.movie.ticket.entity.Show;
import com.movie.ticket.service.MovieService;
import com.movie.ticket.service.ShowService;
import com.movie.ticket.service.TheatreService;

@Controller
public class ShowController {

    private final ShowService showService;
    private final MovieService movieService;
    private final TheatreService theatreService;

    // Constructor Injection
    public ShowController(ShowService showService,
                          MovieService movieService,
                          TheatreService theatreService) {
        this.showService = showService;
        this.movieService = movieService;
        this.theatreService = theatreService;
    }

    // Display All Shows
    @GetMapping("/shows")
    public String viewShows(Model model) {
        model.addAttribute("shows", showService.getAllShows());
        return "shows";
    }

    // Show Add Show Form
    @GetMapping("/shows/add")
    public String showAddShowForm(Model model) {

        model.addAttribute("show", new Show());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());

        return "add-show";
    }

    // Save Show
    @PostMapping("/shows/save")
    public String saveShow(@ModelAttribute("show") Show show) {
        showService.saveShow(show);
        return "redirect:/shows";
    }

    // Show Edit Form
    @GetMapping("/shows/edit/{id}")
    public String showEditShowForm(@PathVariable Long id, Model model) {

        model.addAttribute("show", showService.getShowById(id));
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());

        return "edit-show";
    }

    // Update Show
    @PostMapping("/shows/update")
    public String updateShow(@ModelAttribute("show") Show show) {
        showService.updateShow(show);
        return "redirect:/shows";
    }

    // Delete Show
    @GetMapping("/shows/delete/{id}")
    public String deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return "redirect:/shows";
    }
}