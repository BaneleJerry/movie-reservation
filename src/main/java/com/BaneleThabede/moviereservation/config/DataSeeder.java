package com.BaneleThabede.moviereservation.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.BaneleThabede.moviereservation.dto.ShowtimeRequest;
import com.BaneleThabede.moviereservation.entity.Movie;
import com.BaneleThabede.moviereservation.entity.Reservation;
import com.BaneleThabede.moviereservation.entity.Seat;
import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.ReservationStatus;
import com.BaneleThabede.moviereservation.entity.enums.Role;
import com.BaneleThabede.moviereservation.entity.enums.SeatStatus;
import com.BaneleThabede.moviereservation.repository.MoviesRepository;
import com.BaneleThabede.moviereservation.repository.ReservationRepository;
import com.BaneleThabede.moviereservation.repository.SeatRepository;
import com.BaneleThabede.moviereservation.repository.ShowtimeRepository;
import com.BaneleThabede.moviereservation.repository.UserRepository;
import com.BaneleThabede.moviereservation.service.ShowtimeService;
import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoviesRepository movieRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Faker faker = new Faker();

    @Value("${seeders.users}")
    private int seedUsers;

    @Value("${seeders.movies}")
    private int seedMovies;

    @Value("${seeders.showtimes}")
    private int seedShowtimes;

    @Value("${seeders.reservations}")
    private int seedReservations;

    @Value("${seeders.SeedData}")
    private boolean seedData;

    @Override
    public void run(String... args) throws Exception {
        printSeedInfo();
        if (seedData) {
            seedAdmin();
            seedUsers(seedUsers);
            seedMovies(seedMovies);
            seedShowtimes(seedShowtimes);
            seedReservations(seedReservations);
        }

    }

    private void printSeedInfo() {
        System.out.println("Seeders enabled: " + seedData);
        System.out.println("Seeders users: " + seedUsers);
        System.out.println("Seeders movies: " + seedMovies);
        System.out.println("Seeders showtimes: " + seedShowtimes);
        System.out.println("Seeders reservations: " + seedReservations);
    }

    @Transactional
    public void seedAdmin() {
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Hash password
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }

    @Transactional
    public void seedUsers(int seedUsers) {
        if (userRepository.count() == 0) {
            return;
        }
        List<User> users = new ArrayList<>();
        for (int i = 0; i < seedUsers; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.USER);
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    @Transactional
    public void seedMovies(int seedMovies) {
        if (movieRepository.count() == 0) {
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < seedMovies; i++) {
                Movie movie = new Movie();
                movie.setTitle(faker.book().title());
                movie.setGenre(faker.book().genre());
                movie.setPosterUrl(faker.internet().url());
                movie.setDescription(faker.lorem().paragraph());
                movies.add(movie);
            }
            movieRepository.saveAll(movies);
        }
    }

    public void seedShowtimes(int seedShowtimes) {
        if (showtimeRepository.count() == 0) {
            List<Movie> movies = movieRepository.findAll(); // Fetch the movies list once

            if (movies.isEmpty()) {
                System.out.println("No movies found. Skipping showtimes seeding.");
                return; // Exit if no movies are found
            }

            List<ShowtimeRequest> showtimes = new ArrayList<>();
            for (int i = 0; i < seedShowtimes; i++) {
                Movie movie = movies.get(faker.random().nextInt(movies.size())); // Pick a random movie
                ShowtimeRequest showtime = new ShowtimeRequest();
                showtime.setMovieId(movie.getId());
                showtime.setShowtime(faker.date().future(100, 2, TimeUnit.DAYS).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime());
                showtime.setTotalSeats(faker.random().nextInt(50, 200));
                showtimes.add(showtime);
            }
            showtimeService.addShowtimes(showtimes);
            System.out.println("Showtimes seeded successfully!");
        }
    }

    @Transactional
    public void seedReservations(int seedReservations) {
        if (reservationRepository.count() == 0) {
            List<Showtime> showtimes = showtimeRepository.findAll();
            List<User> users = userRepository.findAll();
            List<Seat> seats = seatRepository.findAll();
            List<Reservation> reservations = new ArrayList<>();

            for (int i = 0; i < seedReservations; i++) {
                // Get a random showtime
                Showtime showtime = showtimes.get(faker.random().nextInt(0, showtimes.size() - 1));

                // Get available seats for that showtime
                List<Seat> showtimeSeats = seats.stream()
                        .filter(seat -> seat.getShowtime().getId().equals(showtime.getId())
                                && seat.getStatus() == SeatStatus.AVAILABLE)
                        .toList();

                if (showtimeSeats.isEmpty())
                    continue; // Skip if no available seats

                // Get a random user
                User user = users.get(faker.random().nextInt(0, users.size() - 1));

                // Pick a random available seat
                Seat seat = showtimeSeats.get(faker.random().nextInt(0, showtimeSeats.size() - 1));
                seat.setStatus(SeatStatus.RESERVED);

                // Create reservation
                Reservation reservation = new Reservation();
                reservation.setShowtime(showtime);
                reservation.setUser(user);
                reservation.setReservationTime(LocalDateTime.now());
                reservation.setSeat(seat);
                reservation.setStatus(ReservationStatus.CONFIRMED);
                reservations.add(reservation);

                // Reduce showtime seat count and save updates
                showtimeRepository.save(showtime);
                seatRepository.save(seat);
            }
            reservationRepository.saveAll(reservations);
        }
    }

}
