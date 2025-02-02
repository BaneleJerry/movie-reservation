package com.BaneleThabede.moviereservation.controller;

import com.BaneleThabede.moviereservation.dto.UserDto;
import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        try {
            User user = userService.registerNewUserAccount(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
    }catch(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }
}
