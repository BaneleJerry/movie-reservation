package com.BaneleThabede.moviereservation.dto;

import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String username;
    private String name;
    private String lastName;
    private String email;
    @JsonIgnore
    private Role role;
    private String Password;

    // Static method to convert User entity to UserDto
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(),user.getUsername(), user.getName(),user.getUsername(), user.getEmail(), user.getRole(), user.getPassword());
    }
}
