package com.movie.ticket.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.ticket.entity.Theatre;
import com.movie.ticket.repository.TheatreRepository;
import com.movie.ticket.service.TheatreService;

@Service
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    // Constructor Injection
    public TheatreServiceImpl(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public Theatre getTheatreById(Long id) {
        return theatreRepository.findById(id).orElse(null);
    }

    @Override
    public Theatre saveTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre updateTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public void deleteTheatre(Long id) {
        theatreRepository.deleteById(id);
    }
}