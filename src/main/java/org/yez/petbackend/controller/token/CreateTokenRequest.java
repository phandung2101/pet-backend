package org.yez.petbackend.controller.token;

import java.time.Instant;

public record CreateTokenRequest(
    String name,
    Instant expiresAt
) {
}
