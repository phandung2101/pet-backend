package org.yez.petbackend.controller.token;

import org.yez.petbackend.domain.token.Token;

import java.time.Instant;
import java.util.UUID;

public record TokenResponse(
    UUID id,
    String token,
    String name,
    Instant expiresAt,
    Instant createdAt
) {
    public TokenResponse(Token token) {
        this(
            token.id(),
            token.token(),
            token.name(),
            token.expiresAt(),
            token.createdAt()
        );
    }
}
