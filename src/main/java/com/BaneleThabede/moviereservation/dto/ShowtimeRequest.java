package com.BaneleThabede.moviereservation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowtimeRequest {
    private UUID id;
    private UUID movieId;
    private LocalDateTime showtime;
    private int totalSeats;
}
