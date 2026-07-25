package com.movie.ticket.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.movie.ticket.entity.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieMovieId(Long movieId);
    List<Show> findByMovieMovieIdAndShowDate(Long movieId, LocalDate showDate);
}