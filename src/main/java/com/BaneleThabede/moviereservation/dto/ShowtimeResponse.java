package com.BaneleThabede.moviereservation.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Seat;
import com.BaneleThabede.moviereservation.entity.enums.ShowtimeStatus;

import lombok.Data;

@Data
public class ShowtimeResponse {
    private UUID id;
    private LocalDateTime showtime;
    private int totalSeats;
    private UUID movieId;
    private String movieTitle;
    private Collection<Seat> seats;
    private ShowtimeStatus status;
}
