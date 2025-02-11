package com.BaneleThabede.moviereservation.repository;

import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Showtime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository extends JpaRepository<Showtime,UUID>{
}
