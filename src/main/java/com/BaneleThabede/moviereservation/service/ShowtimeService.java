package com.BaneleThabede.moviereservation.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.BaneleThabede.moviereservation.dto.ShowtimeResponse;
import com.BaneleThabede.moviereservation.entity.Movie;
import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.exception.MovieNotFoundException;
import com.BaneleThabede.moviereservation.repository.MoviesRepository;
import com.BaneleThabede.moviereservation.repository.ShowtimeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeService {

    @Autowired
    ShowtimeRepository showtimeRepository;

    @Autowired
    MoviesRepository mRepo;

    public Showtime addShowtime(UUID movieId, LocalDateTime showtime, int totalSeats) {
        Optional<Movie> movie = mRepo.findById(movieId);
        if (movie.isEmpty()) {
            throw new MovieNotFoundException("The Movie with the id: '" + movieId + "' does not exits");
        }

        Showtime newShowtime = new Showtime();
        newShowtime.setMovie(movie.get());
        newShowtime.setShowtime(showtime);
        newShowtime.setTotalSeats(totalSeats);

        return showtimeRepository.save(newShowtime);
    }

    public Collection<Showtime> addShowtimes(Collection<Showtime> showtimes) {
        if (showtimes == null || showtimes.isEmpty()) {
            throw new IllegalArgumentException("Show times can not be Empty");
        }
        return showtimeRepository.saveAll(showtimes);
    }

    public Collection<Showtime> getMovies() {
        return showtimeRepository.findAll();
    }

    public ShowtimeResponse mapToShowtimeResponse(Showtime showtime) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setId(showtime.getId());
        response.setShowtime(showtime.getShowtime());
        response.setTotalSeats(showtime.getTotalSeats());
        response.setMovieId(showtime.getMovie().getId());
        response.setMovieTitle(showtime.getMovie().getTitle());
        return response;
    }

}
