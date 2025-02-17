package com.BaneleThabede.moviereservation.dto;

import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Seat;

import lombok.Data;

@Data
public class ReservationRequest {
    private UUID showtimeId;   // ID of the showtime they are booking for
    private Seat seat; // List of seat numbers the user wants to book
}
