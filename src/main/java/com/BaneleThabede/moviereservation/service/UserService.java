package com.BaneleThabede.moviereservation.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.BaneleThabede.moviereservation.repository.UserRepository;
import com.BaneleThabede.moviereservation.config.JwtUtils;
import com.BaneleThabede.moviereservation.dto.LoginRequest;
import com.BaneleThabede.moviereservation.dto.UserDto;
import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.service.userService.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    public User registerNewUserAccount(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUsername();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User updateUserDetails(UserDto userDto) {
        Optional<User> userOpt = userRepository.findById(userDto.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            return userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found with ID: " + userDto.getId());
        }
    }

    public boolean changeUserPassword(UUID userId, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (encoder.matches(oldPassword, user.getPassword())) { // Check old password
                String newPasswordHash = encoder.encode(newPassword); // Hash the new password
                user.setPassword(newPasswordHash);
                userRepository.save(user);
                return true;
            } else {
                // Log incorrect old password attempt
                throw new InvalidPasswordException("Incorrect old password"); // Or return false
                // return false;
            }
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId); // Or return false
            // return false;
        }
    }

    // Custom Exception for User Not Found
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    // Custom Exception for Invalid Password
    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
