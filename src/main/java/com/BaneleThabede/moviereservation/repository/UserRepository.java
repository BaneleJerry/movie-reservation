package com.BaneleThabede.moviereservation.repository;

import java.util.Optional;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByRole(Role role);
}
