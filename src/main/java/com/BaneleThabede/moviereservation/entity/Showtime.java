package com.BaneleThabede.moviereservation.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="showtime")
public class Showtime {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movie Movie;

    @Column(nullable=false)
    private LocalDateTime showtime;

    @Column(nullable=false)
    private int totalSeats;

    @OneToMany(mappedBy="showtime", cascade= CascadeType.ALL)
    private Seat seat;

}

