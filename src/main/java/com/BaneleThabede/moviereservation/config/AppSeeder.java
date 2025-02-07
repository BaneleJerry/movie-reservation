package com.BaneleThabede.moviereservation.config;

import com.BaneleThabede.moviereservation.Repository.UserRepository;
import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppSeeder implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
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
}
