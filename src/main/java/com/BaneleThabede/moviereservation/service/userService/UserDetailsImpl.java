package com.BaneleThabede.moviereservation.service.userService;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import com.BaneleThabede.moviereservation.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails{
 private static final long serialVersionUID = 1L;

    private UUID id;

    private String username;

    @JsonIgnore
    private String password;

    public UserDetailsImpl(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;

    }

    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword());
    }

    public UUID getId() {
        return id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
