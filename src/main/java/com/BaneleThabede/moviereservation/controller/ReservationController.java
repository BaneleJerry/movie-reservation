package com.BaneleThabede.moviereservation.controller;

import com.BaneleThabede.moviereservation.dto.ReservationRequest;
import com.BaneleThabede.moviereservation.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping()
    public ResponseEntity<?> addReservation(@RequestBody ReservationRequest request) {
        return new ResponseEntity<>(reservationService.createReservation(request), HttpStatus.CREATED);
    }


    @GetMapping()
    public ResponseEntity<?> getReservations(){
        return ResponseEntity.ok(reservationService.getReservations());
    }
}
