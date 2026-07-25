package com.movie.ticket.service;

import java.util.List;

import com.movie.ticket.entity.Show;

public interface ShowService {

    List<Show> getAllShows();

    Show getShowById(Long id);

    Show saveShow(Show show);

    Show updateShow(Show show);

    void deleteShow(Long id);

}