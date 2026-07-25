package com.movie.ticket.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.ticket.entity.Movie;
import com.movie.ticket.entity.Show;
import com.movie.ticket.service.MovieService;
import com.movie.ticket.service.ShowService;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final ShowService showService;

    public MovieController(MovieService movieService, ShowService showService) {
        this.movieService = movieService;
        this.showService = showService;
    }

    // Home Page - BookMyShow Main Landing Page
    @GetMapping("/")
    public String homePage(@RequestParam(value = "genre", required = false) String genre,
                           @RequestParam(value = "language", required = false) String language,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) {

        List<Movie> movies = movieService.getAllMovies();

        if (search != null && !search.trim().isEmpty()) {
            String term = search.trim().toLowerCase();
            movies = movies.stream()
                    .filter(m -> m.getTitle().toLowerCase().contains(term)
                            || m.getGenre().toLowerCase().contains(term)
                            || (m.getCast() != null && m.getCast().toLowerCase().contains(term)))
                    .toList();
        } else {
            if (genre != null && !genre.trim().isEmpty() && !"All".equalsIgnoreCase(genre)) {
                movies = movies.stream()
                        .filter(m -> m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                        .toList();
            }
            if (language != null && !language.trim().isEmpty() && !"All".equalsIgnoreCase(language)) {
                movies = movies.stream()
                        .filter(m -> m.getLanguage().toLowerCase().contains(language.toLowerCase()))
                        .toList();
            }
        }

        List<Movie> trendingMovies = movieService.getAllMovies().stream()
                .filter(m -> m.getBannerUrl() != null && !m.getBannerUrl().isEmpty())
                .limit(4)
                .toList();

        model.addAttribute("movies", movies);
        model.addAttribute("trendingMovies", trendingMovies);
        model.addAttribute("selectedGenre", genre != null ? genre : "All");
        model.addAttribute("selectedLanguage", language != null ? language : "All");
        model.addAttribute("searchTerm", search != null ? search : "");

        return "index";
    }

    // Display All Movies (Table View)
    @GetMapping("/movies")
    public String viewMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    // Display Movies as Cards
    @GetMapping("/movies/cards")
    public String movieCards(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movie-cards";
    }

    // View Movie Details with Showtimes Selection
    @GetMapping("/movies/details/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/";
        }

        List<Show> shows = showService.getAllShows().stream()
                .filter(s -> s.getMovie().getMovieId().equals(id))
                .toList();

        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);

        return "movie-details";
    }

    // Admin: Show Add Movie Form
    @GetMapping("/movies/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    // Admin: Save New Movie
    @PostMapping("/movies/save")
    public String saveMovie(@ModelAttribute("movie") Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    // Admin: Show Edit Movie Form
    @GetMapping("/movies/edit/{id}")
    public String showEditMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "edit-movie";
    }

    // Admin: Update Movie
    @PostMapping("/movies/update")
    public String updateMovie(@ModelAttribute("movie") Movie movie) {
        movieService.updateMovie(movie);
        return "redirect:/movies";
    }

    // Admin: Delete Movie
    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}