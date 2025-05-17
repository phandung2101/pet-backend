package org.yez.petbackend.domain.token;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Token(
        UUID id,
        String token,
        String name,
        UUID userId,
        Instant expiresAt,
        Instant createdAt
) {

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }
}
