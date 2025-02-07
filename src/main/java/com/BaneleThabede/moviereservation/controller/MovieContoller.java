package com.BaneleThabede.moviereservation.controller;

import java.util.Collection;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.Movie;
import com.BaneleThabede.moviereservation.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class MovieContoller {

    @Autowired
    MovieService mService;

    @GetMapping()
    public ResponseEntity<?> getAllMovies() {
        return ResponseEntity.ok(mService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable UUID id) {
        return ResponseEntity.ok(mService.getMovieById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getMovieByTitle(@PathVariable String title) {
        return ResponseEntity.ok(mService.getMovieByTitle(title));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> addMovie(@RequestBody Movie movie){
        return ResponseEntity.ok(mService.addMovie(movie));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/batch")
    public ResponseEntity<?> addMovies(@RequestBody Collection<Movie> movies){
        return ResponseEntity.ok(mService.addMovies(movies));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie){
        return ResponseEntity.status(HttpStatus.CREATED).body(mService.updateMovie(movie));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id){
        mService.deleteById(id);
        return ResponseEntity.ok("Movie has been Delete Succesfully");
    }
    
}
