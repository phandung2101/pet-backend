package org.yez.petbackend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yez.petbackend.domain.user.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record PetUser(
        UUID id,
        String username,
        String password,
        UserRole role
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
