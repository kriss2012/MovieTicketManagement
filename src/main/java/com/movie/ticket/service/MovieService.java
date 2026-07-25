package com.movie.ticket.service;

import java.util.List;

import com.movie.ticket.entity.Movie;

public interface MovieService {

    List<Movie> getAllMovies();

    Movie getMovieById(Long id);

    Movie saveMovie(Movie movie);

    Movie updateMovie(Movie movie);

    void deleteMovie(Long id);

}