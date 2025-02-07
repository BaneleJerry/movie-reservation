package com.BaneleThabede.moviereservation.controller;

import com.BaneleThabede.moviereservation.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/")
public class AdminController{
   
    @Autowired
    UserRepository userRepo;

    @GetMapping("/users")
    ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userRepo.findAll());
    }
}
