package com.BaneleThabede.moviereservation.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.BaneleThabede.moviereservation.dto.ReservationRequest;
import com.BaneleThabede.moviereservation.dto.ReservationResponse;
import com.BaneleThabede.moviereservation.entity.Reservation;
import com.BaneleThabede.moviereservation.entity.Seat;
import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.ReservationStatus;
import com.BaneleThabede.moviereservation.entity.enums.SeatStatus;
import com.BaneleThabede.moviereservation.entity.enums.ShowtimeStatus;
import com.BaneleThabede.moviereservation.repository.ReservationRepository;
import com.BaneleThabede.moviereservation.repository.SeatRepository;
import com.BaneleThabede.moviereservation.repository.ShowtimeRepository;
import com.BaneleThabede.moviereservation.repository.UserRepository;
import com.BaneleThabede.moviereservation.utils.AuthUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        if (showtime.getStatus().equals(ShowtimeStatus.FINISHED)) {
            throw new RuntimeException("Showtime has already passed");
        }

        if(showtime.getStatus().equals(ShowtimeStatus.UPCOMING)){
            throw new RuntimeException("Showtime is going to be available for booking in the future");
        }

        if (reservationRepository.existsByShowtime_IdAndSeat_Id(request.getShowtimeId(), request.getSeat().getId())) {
            throw new RuntimeException("Seat already reserved");
        }

        Seat seat = seatRepository.findById(request.getSeat().getId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() == SeatStatus.RESERVED) {
            throw new RuntimeException("Seat already reserved");
        }

        Reservation reservation = new Reservation();
        reservation.setShowtime(showtime);
        reservation.setSeat(seat);
        reservation.setUser(getCurrentUser());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationTime(LocalDateTime.now());

        seat.setStatus(SeatStatus.RESERVED);
        return (mapToResponse(reservationRepository.save(reservation)));
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse cancelReservation(ReservationRequest request) {
        if (request == null || request.getId() == null) {
            throw new RuntimeException("The request body cannot be null or empty");
        }

        // Find reservation by ID
        Reservation reservation = reservationRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Reservation cannot be found"));

        if(!(reservation.getUser().getId().equals(getCurrentUser().getId()))){
            throw new RuntimeException("User is not Authicated");
        }
        // Update reservation status to CANCELED
        reservation.setStatus(ReservationStatus.CANCELED);

        // Update seat status to AVAILABLE
        Seat seat = reservation.getSeat();
        seat.setStatus(SeatStatus.AVAILABLE);

        // Save the reservation update
        Reservation savedReservations = reservationRepository.save(reservation);

        // Construct and return a response
        return mapToResponse(savedReservations);
    }

    public Collection<ReservationResponse> getUserReservation() {
        return reservationRepository.findByUserId(getCurrentUser().getId())
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        return userRepository.findByUsername(AuthUtils.getCurrentUser())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getShowtime().getId(),
                reservation.getShowtime().getMovie().getTitle(),
                reservation.getShowtime().getShowtime(),
                reservation.getReservationTime(),
                reservation.getSeat());
    }
}
