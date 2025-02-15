package com.BaneleThabede.moviereservation.controller;

import java.util.Collection;
import java.util.UUID;

import com.BaneleThabede.moviereservation.dto.ShowtimeRequest;
import com.BaneleThabede.moviereservation.dto.ShowtimeResponse;
import com.BaneleThabede.moviereservation.service.ShowtimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showtime")
public class ShowtimeController {

    @Autowired
    ShowtimeService showSevice;

    @GetMapping()
    public ResponseEntity<?> getShowtimes() {
        return ResponseEntity.ok(showSevice.getShowtimes());
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> getShowtimeById(@PathVariable UUID showtimeId) {
        return ResponseEntity.ok(showSevice.getShowtime(showtimeId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addShowtime(@RequestBody ShowtimeRequest request) {
        ShowtimeResponse newShowtime = showSevice.addShowtime(request);
        return ResponseEntity.ok(newShowtime);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addShowtimes(@RequestBody Collection<ShowtimeRequest> showtimes){
        Collection<ShowtimeResponse> savedShowtime = showSevice.addShowtimes(showtimes);
        return ResponseEntity.ok(savedShowtime);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getShowtimesByMovieId(@PathVariable UUID movieId){
        return ResponseEntity.ok(showSevice.getShowtimesByMovie(movieId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteShowtime(UUID showtimeId){
        showSevice.deleteShowtime(showtimeId);
        return ResponseEntity.ok("Showtime deleted");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateShowtime(@RequestBody ShowtimeRequest request){
        ShowtimeResponse updatedShowtime = showSevice.updateShowtime(request);
        return ResponseEntity.ok(updatedShowtime);
    }
}
