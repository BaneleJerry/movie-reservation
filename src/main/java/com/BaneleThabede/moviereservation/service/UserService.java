package com.BaneleThabede.moviereservation.service;

import java.util.Optional;
import java.util.UUID;

import com.BaneleThabede.moviereservation.Repository.UserRepository;
import com.BaneleThabede.moviereservation.dto.UserDto;
import com.BaneleThabede.moviereservation.entity.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // Constructor Injection (Best Practice)
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User registerNewUserAccount(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            String passwordHash = userOpt.get().getPasswordHash();
            return passwordEncoder.matches(password, passwordHash);
        }
        return false;
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

            if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) { // Check old password
                String newPasswordHash = passwordEncoder.encode(newPassword); // Hash the new password
                user.setPasswordHash(newPasswordHash);
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
}
