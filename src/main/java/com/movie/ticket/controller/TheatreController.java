package com.movie.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.movie.ticket.entity.Theatre;
import com.movie.ticket.service.TheatreService;

@Controller
public class TheatreController {

    private final TheatreService theatreService;

    // Constructor Injection
    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    // Display All Theatres
    @GetMapping("/theatres")
    public String viewTheatres(Model model) {
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "theatres";
    }

    // Show Add Theatre Form
    @GetMapping("/theatres/add")
    public String showAddTheatreForm(Model model) {
        model.addAttribute("theatre", new Theatre());
        return "add-theatre";
    }

    // Save Theatre
    @PostMapping("/theatres/save")
    public String saveTheatre(@ModelAttribute("theatre") Theatre theatre) {
        theatreService.saveTheatre(theatre);
        return "redirect:/theatres";
    }

    // Show Edit Theatre Form
    @GetMapping("/theatres/edit/{id}")
    public String showEditTheatreForm(@PathVariable Long id, Model model) {

        Theatre theatre = theatreService.getTheatreById(id);

        model.addAttribute("theatre", theatre);

        return "edit-theatre";
    }

    // Update Theatre
    @PostMapping("/theatres/update")
    public String updateTheatre(@ModelAttribute("theatre") Theatre theatre) {
        theatreService.updateTheatre(theatre);
        return "redirect:/theatres";
    }

    // Delete Theatre
    @GetMapping("/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return "redirect:/theatres";
    }

}