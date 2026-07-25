package com.movie.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "theatres")
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;

    @Column(name = "theatre_name")
    private String theatreName;

    @Column(name = "name")
    private String name;

    private String location;

    private String city;

    private Integer totalSeats;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "active")
    private Boolean active = true;

    // Default Constructor
    public Theatre() {
    }

    // Parameterized Constructor
    public Theatre(Long theatreId, String theatreName, String location, String city, Integer totalSeats) {
        this.theatreId = theatreId;
        this.theatreName = theatreName;
        this.name = theatreName;
        this.location = location;
        this.city = city;
        this.totalSeats = totalSeats;
    }

    @PrePersist
    @PreUpdate
    public void syncFields() {
        if (this.theatreName != null && (this.name == null || this.name.trim().isEmpty())) {
            this.name = this.theatreName;
        } else if (this.name != null && (this.theatreName == null || this.theatreName.trim().isEmpty())) {
            this.theatreName = this.name;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
        this.active = this.isActive;
    }

    // Getters and Setters

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName != null ? theatreName : name;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
        if (this.name == null) {
            this.name = theatreName;
        }
    }

    public String getName() {
        return name != null ? name : theatreName;
    }

    public void setName(String name) {
        this.name = name;
        if (this.theatreName == null) {
            this.theatreName = name;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
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

    @Override
    public String toString() {
        return "Theatre{" +
                "theatreId=" + theatreId +
                ", theatreName='" + theatreName + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", totalSeats=" + totalSeats +
                '}';
    }
}