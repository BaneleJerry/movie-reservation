package com.BaneleThabede.moviereservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.BaneleThabede.moviereservation.dto.ShowtimeRequest;
import com.BaneleThabede.moviereservation.dto.ShowtimeResponse;
import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.service.ShowtimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showtime")
public class ShowtimeController {

    @Autowired
    ShowtimeService showSevice;

    @Autowired

    @GetMapping()
    public ResponseEntity<List<ShowtimeResponse>> getShowtime() {
        List<ShowtimeResponse> showtimes = showSevice.getMovies().stream()
                .map(showSevice::mapToShowtimeResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(showtimes);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addShowtime(@RequestBody ShowtimeRequest request) {
        Showtime newShowtime = showSevice.addShowtime(request.getMovieId(), request.getShowtime(),
                request.getTotalSeats());
        return ResponseEntity.ok(showSevice.mapToShowtimeResponse(newShowtime));
    }
}
