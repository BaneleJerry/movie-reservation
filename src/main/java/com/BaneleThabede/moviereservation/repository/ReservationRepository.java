package com.BaneleThabede.moviereservation.repository;

import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    boolean existsByShowtime_IdAndSeat_Id(UUID showtimeId, UUID id);

}
