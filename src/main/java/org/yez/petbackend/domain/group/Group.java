package org.yez.petbackend.domain.group;

import java.util.Set;
import java.util.UUID;

public record Group(
        UUID id,
        String name,
        Set<UUID> memberIds,
        UUID ownerId,
        String description
) {
}
