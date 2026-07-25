package com.movie.ticket.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private LocalDate showDate;

    private LocalTime showTime;

    private Double ticketPrice;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "active")
    private Boolean active = true;

    public Show() {
    }

    public Show(Long showId, Movie movie, Theatre theatre,
                LocalDate showDate, LocalTime showTime,
                Double ticketPrice, Integer availableSeats) {
        this.showId = showId;
        this.movie = movie;
        this.theatre = theatre;
        this.showDate = showDate;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
        this.totalSeats = availableSeats;
    }

    @PrePersist
    @PreUpdate
    public void syncFields() {
        if (this.availableSeats == null && this.totalSeats != null) {
            this.availableSeats = this.totalSeats;
        } else if (this.totalSeats == null && this.availableSeats != null) {
            this.totalSeats = this.availableSeats;
        } else if (this.totalSeats == null && this.availableSeats == null && this.theatre != null) {
            this.totalSeats = this.theatre.getTotalSeats();
            this.availableSeats = this.theatre.getTotalSeats();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
        this.active = this.isActive;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getAvailableSeats() {
        return availableSeats != null ? availableSeats : totalSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
        if (this.totalSeats == null) {
            this.totalSeats = availableSeats;
        }
    }

    public Integer getTotalSeats() {
        return totalSeats != null ? totalSeats : availableSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
        if (this.availableSeats == null) {
            this.availableSeats = totalSeats;
        }
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.active = isActive;
    }

    public Boolean getActive() {
        return active != null ? active : isActive;
    }

    public void setActive(Boolean active) {
        this.active = active;
        this.isActive = active;
    }
}