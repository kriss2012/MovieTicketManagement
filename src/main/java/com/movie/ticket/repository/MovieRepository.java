package com.movie.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.ticket.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}