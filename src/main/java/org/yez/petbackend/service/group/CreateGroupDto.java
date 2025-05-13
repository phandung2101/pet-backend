package org.yez.petbackend.service.group;

import java.util.UUID;

record CreateGroupDto(
        UUID ownerId,
        String name,
        String description
) {
}
