package com.BaneleThabede.moviereservation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private UUID reservationId; // Unique reservation ID
    private UUID userId;        // ID of the user who made the reservation
    private UUID showtimeId;    // Showtime ID
    private String movieTitle;  // Movie title
    private LocalDateTime showtime; // Date and time of the show
    private LocalDateTime createdAt; // Date and time the reservation was made
    private Seat seat; // List of reserved seat numbers
}
