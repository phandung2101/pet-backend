package org.yez.petbackend.domain.user;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record User(
        UUID id,
        String username,
        String password,
        String email,
        String fullName,
        UserRole role,
        boolean approved,
        Set<UUID> groupIds
) {
}
