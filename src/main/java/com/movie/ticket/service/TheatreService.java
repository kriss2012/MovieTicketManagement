package com.movie.ticket.service;

import java.util.List;

import com.movie.ticket.entity.Theatre;

public interface TheatreService {

    List<Theatre> getAllTheatres();

    Theatre getTheatreById(Long id);

    Theatre saveTheatre(Theatre theatre);

    Theatre updateTheatre(Theatre theatre);

    void deleteTheatre(Long id);

}