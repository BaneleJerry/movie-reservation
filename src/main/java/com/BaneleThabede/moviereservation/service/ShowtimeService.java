package com.BaneleThabede.moviereservation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.BaneleThabede.moviereservation.dto.ShowtimeRequest;
import com.BaneleThabede.moviereservation.dto.ShowtimeResponse;
import com.BaneleThabede.moviereservation.entity.Movie;
import com.BaneleThabede.moviereservation.entity.Seat;
import com.BaneleThabede.moviereservation.entity.Showtime;
import com.BaneleThabede.moviereservation.entity.enums.SeatStatus;
import com.BaneleThabede.moviereservation.entity.enums.ShowtimeStatus;
import com.BaneleThabede.moviereservation.exception.MovieNotFoundException;
import com.BaneleThabede.moviereservation.repository.MoviesRepository;
import com.BaneleThabede.moviereservation.repository.SeatRepository;
import com.BaneleThabede.moviereservation.repository.ShowtimeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowtimeService {

    @Autowired
    ShowtimeRepository showtimeRepository;

    @Autowired
    MoviesRepository mRepo;

    @Autowired
    SeatRepository sRepo;

    /**
     * Adds a single showtime to the database.
     * Fetches the associated movie and creates a new Showtime entity.
     * If the movie does not exist, it throws a MovieNotFoundException.
     * After saving the showtime, it generates and saves seats for the showtime.
     *
     * @param request a ShowtimeRequest DTO
     * @return a ShowtimeResponse DTO containing showtime details
     */
    @Transactional
    public ShowtimeResponse addShowtime(ShowtimeRequest request) {

        if (!validateShowtimeDetails(request)) {
            throw new IllegalArgumentException("Invalid showtime details");
        }

        Movie movie = FindMovieById(request.getMovieId());

        Showtime newShowtime = new Showtime();
        newShowtime.setMovie(movie);
        newShowtime.setShowtime(request.getShowtime());
        newShowtime.setTotalSeats(request.getTotalSeats());

        Showtime savedShowtime = showtimeRepository.save(newShowtime);
        preCreateSeats(savedShowtime);
        return mapToShowtimeResponse(savedShowtime);
    }

    /**
     * Adds a collection of showtimes to the database.
     * For each showtime, it fetches the associated movie and creates a new Showtime
     * entity.
     * If the movie does not exist, it skips the showtime.
     * After saving all showtimes, it generates and saves seats for each showtime.
     *
     * @param showtimes a collection of ShowtimeRequest DTOs
     * @return a collection of ShowtimeResponse DTOs
     */
    @Transactional
    public Collection<ShowtimeResponse> addShowtimes(Collection<ShowtimeRequest> showtimes) {
        // Extract all requested movie IDs
        Set<UUID> movieIds = showtimes.stream()
                .map(ShowtimeRequest::getMovieId)
                .collect(Collectors.toSet());

        // Fetch all movies in one query and convert to a Map for quick lookup
        Map<UUID, Movie> movieMap = mRepo.findAllById(movieIds).stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));

        List<Showtime> newShowtimes = new ArrayList<>();
        List<Seat> seats = new ArrayList<>();

        for (ShowtimeRequest request : showtimes) {
            Movie movie = movieMap.get(request.getMovieId());

            if (movie == null) {
                System.err.println("Skipping: Movie with ID " + request.getMovieId() + " does not exist.");
                continue; // Skip this showtime if the movie doesn't exist
            }

            Showtime newShowtime = new Showtime();
            newShowtime.setMovie(movie);
            newShowtime.setShowtime(request.getShowtime());
            newShowtime.setTotalSeats(request.getTotalSeats());
            newShowtimes.add(newShowtime);
        }

        List<Showtime> savedShowtimes = showtimeRepository.saveAll(newShowtimes);

        for (Showtime showtime : savedShowtimes) {
            seats.addAll(generateSeats(showtime));
        }

        sRepo.saveAll(seats);
        return savedShowtimes.stream().map(this::mapToShowtimeResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves all showtimes from the database.
     * Converts each Showtime entity into a ShowtimeResponse DTO.
     *
     * @return a collection of ShowtimeResponse DTOs
     */
    public Collection<ShowtimeResponse> getShowtimes() {
        return showtimeRepository.findAll().stream().map(this::mapToShowtimeResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves a single showtime from the database.
     * Converts the Showtime entity into a ShowtimeResponse DTO.
     *
     * @param id the UUID of the showtime to retrieve
     * @return a ShowtimeResponse DTO containing showtime details
     */
    public ShowtimeResponse getShowtime(UUID id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Showtime with id '" + id + "' does not exist"));
        return mapToShowtimeResponse(showtime);
    }

    /**
     * Retrieves all showtimes associated with a specific movie.
     * Converts each Showtime entity into a ShowtimeResponse DTO.
     *
     * @param movieId the UUID of the movie to retrieve showtimes for
     * @return a collection of ShowtimeResponse DTOs
     */
    public Collection<ShowtimeResponse> getShowtimesByMovie(UUID movieId) {
        return showtimeRepository.findByMovie_Id(movieId).stream()
                .map(this::mapToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public Collection<ShowtimeResponse> getShowtimesByMovieAndDate(UUID movieId, LocalDateTime date) {
        return showtimeRepository.findByMovie_IdAndShowtimeAfter(movieId, date).stream()
                .map(this::mapToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public Collection<ShowtimeResponse> getShowtimesByDate(LocalDateTime date) {
        return showtimeRepository.findByShowtimeAfter(date).stream()
                .map(this::mapToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public Collection<ShowtimeResponse> getShowtimesByDateRange(LocalDateTime start, LocalDateTime end) {
        return showtimeRepository.findByShowtimeBetween(start, end).stream()
                .map(this::mapToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public Collection<ShowtimeResponse> getShowtimesByMovieAndDateRange(UUID movieId, LocalDateTime start,
            LocalDateTime end) {
        return showtimeRepository.findByMovie_IdAndShowtimeBetween(movieId, start, end).stream()
                .map(this::mapToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public ShowtimeResponse updateShowtime(ShowtimeRequest request) {
        if (!validateShowtimeDetails(request)) {
            throw new IllegalArgumentException("Invalid showtime details");
        }

        Showtime showtime = showtimeRepository.findById(request.getId())
                .orElseThrow(
                        () -> new MovieNotFoundException("Showtime with id '" + request.getId() + "' does not exist"));

        Movie movie = mRepo.findById(request.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with ID: " + request.getMovieId()));

        showtime.setMovie(movie);
        showtime.setShowtime(request.getShowtime());
        showtime.setTotalSeats(request.getTotalSeats());

        Showtime savedShowtime = showtimeRepository.save(showtime);
        preCreateSeats(savedShowtime);
        return mapToShowtimeResponse(savedShowtime);
    }

    public void deleteShowtime(UUID id) {
        showtimeRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Showtime with id '" + id + "' does not exist"));
        showtimeRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateShowtimeStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<Showtime> upcomingshowtimes = showtimeRepository.findByStatus(ShowtimeStatus.UPCOMING);
        for (Showtime showtime : upcomingshowtimes) {
            if (showtime.getShowtime().isBefore(now)) {
                showtime.setStatus(ShowtimeStatus.IN_PROGRESS);
            }
        }

        List<Showtime> inProgressShowtimes = showtimeRepository.findByStatus(ShowtimeStatus.IN_PROGRESS);
        for (Showtime showtime : inProgressShowtimes) {
            if (showtime.getShowtime().plusHours(3).isBefore(now)) { // Assuming 2-hour movies
                showtime.setStatus(ShowtimeStatus.FINISHED);
            }
        }

        showtimeRepository.saveAll(upcomingshowtimes);
        showtimeRepository.saveAll(inProgressShowtimes);
    }

    /**
     * Maps a Showtime entity to a ShowtimeResponse DTO.
     * Converts a Showtime entity into a structured response object
     * containing relevant showtime and movie details.
     *
     * @param showtime the Showtime entity to map
     * @return a ShowtimeResponse DTO containing showtime details
     */
    private ShowtimeResponse mapToShowtimeResponse(Showtime showtime) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setId(showtime.getId());
        response.setShowtime(showtime.getShowtime());
        response.setTotalSeats(showtime.getTotalSeats());
        response.setMovieId(showtime.getMovie().getId());
        response.setMovieTitle(showtime.getMovie().getTitle());
        response.setSeats(showtime.getSeats());
        response.setStatus(showtime.getStatus());
        return response;
    }

    /**
     * Pre-creates seats for a given showtime.
     * If seats already exist for the showtime, it retrieves them;
     * otherwise, it generates and saves new seats.
     *
     * @param showtime the Showtime entity for which seats are pre-created
     * @return a collection of Seat entities associated with the showtime
     */
    private Collection<Seat> preCreateSeats(Showtime showtime) {
        if (sRepo.existsByShowtime(showtime)) {
            System.out.println("Seats already exits for showtime:" + showtime.getId());
            return sRepo.findByShowtime(showtime);
        }
        return sRepo.saveAll(generateSeats(showtime));
    }

    /**
     * Generates a list of seats for a given showtime.
     * Creates seat entities based on the total number of seats and
     * assigns them a sequential seat number.
     *
     * @param showtime the Showtime entity for which seats are generated
     * @return a list of newly created Seat entities
     */
    private List<Seat> generateSeats(Showtime showtime) {
        return IntStream.rangeClosed(1, showtime.getTotalSeats())
                .mapToObj(i -> {
                    Seat seat = new Seat();
                    seat.setShowtime(showtime);
                    seat.setSeatNumber("S" + i);
                    seat.setStatus(SeatStatus.AVAILABLE);
                    return seat;
                })
                .collect(Collectors.toList());
    }

    /**
     * Validates the details of a showtime request.
     * Ensures that the movie ID, showtime, and total seats are provided.
     * Validates that the showtime does not already exist for the movie.
     *
     * @param request a ShowtimeRequest DTO
     * @return true if all validations pass
     */
    protected boolean validateShowtimeDetails(ShowtimeRequest request) {
        if (request.getMovieId() == null) {
            throw new IllegalArgumentException("Movie ID is required");
        }
        if (request.getShowtime() == null) {
            throw new IllegalArgumentException("Showtime is required");
        }
        if (request.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Total seats must be greater than 0");
        }

        // Validate that the showtime does not already exist for the movie
        mRepo.findById(request.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with ID: " + request.getMovieId()));

        return true; // Return true only if all validations pass
    }

    private boolean validateByMovieAndShowtime(UUID movieId, LocalDateTime showtime) {
        return showtimeRepository.existsByMovie_IdAndShowtime(movieId, showtime);
    }

    private Movie FindMovieById(UUID movieId) {
        return mRepo.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID: " + movieId));
    }

}
