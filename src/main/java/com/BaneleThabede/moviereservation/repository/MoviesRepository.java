package com.BaneleThabede.moviereservation.repository;

import java.util.Optional;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends JpaRepository<Movie,UUID> {

    Optional<Movie> findByTitle(String title);
    
}
