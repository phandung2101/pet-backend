package org.yez.petbackend.service.auth;

public record LoginRequestDto(
        String username,
        String password
) {
}
