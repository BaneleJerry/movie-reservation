package com.BaneleThabede.moviereservation.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    /**
     * Get the current user's ID
     * If the user is not authenticated, a RuntimeException is thrown
     *
     * @return the current user's ID
     */
    public static String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return auth.getName();
        }
        throw new RuntimeException("User Not Authenticated");
    }
}
