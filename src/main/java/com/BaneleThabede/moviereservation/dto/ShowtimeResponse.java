package com.BaneleThabede.moviereservation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowtimeResponse {
    private UUID id;
    private LocalDateTime showtime;
    private int totalSeats;
    private UUID movieId;
    private String movieTitle;
}
