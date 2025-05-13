package org.yez.petbackend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yez.petbackend.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record PetUser(
        User user
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.role());
    }

    public UUID getId() {
        return user.id();
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public String getUsername() {
        return user.username();
    }
}
