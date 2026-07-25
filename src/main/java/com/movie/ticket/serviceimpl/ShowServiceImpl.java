package com.movie.ticket.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.ticket.entity.Show;
import com.movie.ticket.repository.ShowRepository;
import com.movie.ticket.service.ShowService;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    public ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public Show getShowById(Long id) {
        return showRepository.findById(id).orElse(null);
    }

    @Override
    public Show saveShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    public Show updateShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }
}