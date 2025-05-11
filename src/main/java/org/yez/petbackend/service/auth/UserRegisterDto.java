package org.yez.petbackend.service.auth;

public record UserRegisterDto(
        String username,
        String password,
        String email,
        String fullName
) {
}
