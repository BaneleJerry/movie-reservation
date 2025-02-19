package com.BaneleThabede.moviereservation.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.BaneleThabede.moviereservation.entity.enums.ShowtimeStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonBackReference
    private Movie movie;

    @Column(nullable=false)
    private LocalDateTime showtime;

    @Column(nullable=false)
    private int totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowtimeStatus status = ShowtimeStatus.UPCOMING;
    
    @OneToMany(mappedBy="showtime", cascade= CascadeType.ALL)
    @JsonManagedReference
    private List<Seat> seats;

}

