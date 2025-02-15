package com.BaneleThabede.moviereservation.utils;

import java.util.UUID;

import com.BaneleThabede.moviereservation.service.userService.UserDetailsImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    /**
     * Get the current user's ID
     * If the user is not authenticated, a RuntimeException is thrown
     *
     * @return the current user's ID
     */
    public static UUID getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl)) {
            return ((UserDetailsImpl) auth.getPrincipal()).getId();
        }
        throw new RuntimeException("User Not Authenticated");

    }
}
