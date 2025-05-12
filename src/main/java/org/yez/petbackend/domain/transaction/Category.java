package org.yez.petbackend.domain.transaction;

import java.util.UUID;

public record Category(
        UUID id,
        String name
) {
}
