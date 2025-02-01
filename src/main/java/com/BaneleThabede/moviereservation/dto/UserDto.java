package com.BaneleThabede.moviereservation.dto;

import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.User;
import com.BaneleThabede.moviereservation.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private String Password;

    // Static method to convert User entity to UserDto
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getPasswordHash());
    }
}
