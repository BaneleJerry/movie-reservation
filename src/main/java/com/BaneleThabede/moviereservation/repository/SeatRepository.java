package com.BaneleThabede.moviereservation.repository;

import java.util.Collection;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Seat;
import com.BaneleThabede.moviereservation.entity.Showtime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, UUID>{

    Collection<Seat> findByShowtime(Showtime showtime);

    boolean existsByShowtime(Showtime showtime);

}
