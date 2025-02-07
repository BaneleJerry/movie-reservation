package com.BaneleThabede.moviereservation.service;

import java.util.Collection;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Movie;
import com.BaneleThabede.moviereservation.exception.MovieNotFoundException;
import com.BaneleThabede.moviereservation.repository.MoviesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MovieService {

    @Autowired
    private MoviesRepository mRepository;

    public Movie addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        return mRepository.save(movie);
    }

    public Collection<?> addMovies(Collection<Movie> movies){
        if(movies.isEmpty() || movies == null){
            throw new IllegalArgumentException("Movies can not be empty or null");
        }
        return mRepository.saveAll(movies);
    }

    public Movie updateMovie(Movie movie) {
        if (movie == null || movie.getId() == null) {
            throw new IllegalArgumentException("Movie or Movie ID cannot be null");
        }
        if (!mRepository.existsById(movie.getId())) {
            throw new MovieNotFoundException("Movie with ID " + movie.getId() + " does not exist");
        }
        return mRepository.save(movie);
    }

    public Movie getMovieById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return mRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found"));
    }

    public Movie getMovieByTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Title cannot be empty or null");
        }
        return mRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Movie with title '" + title + "' not found"));
    }

    public Collection<?> getAllMovies() {
        return mRepository.findAll();
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Movie Id Must be Provided");
        }
        if(!mRepository.existsById(id)) {
            throw new MovieNotFoundException("Movie with ID :'" + id + "' was not Found");
        }
        mRepository.deleteById(id);
    }

}
