package com.BaneleThabede.moviereservation.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.entity.enums.ShowtimeStatus;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {

    Collection<Showtime> findByMovie_Id(UUID movieId);  

    Collection<Showtime> findByShowtimeBetween(LocalDateTime start, LocalDateTime end);

    Collection<Showtime> findByMovie_IdAndShowtimeBetween(UUID movieId, LocalDateTime start, LocalDateTime end);  

    Collection<Showtime> findByShowtimeAfter(LocalDateTime date);

    Collection<Showtime> findByMovie_IdAndShowtimeAfter(UUID movieId, LocalDateTime date);  

    boolean existsByMovie_IdAndShowtime(UUID movieId, LocalDateTime showtime);

    List<Showtime> findByStatus(ShowtimeStatus inProgress);  
}

