package org.yez.petbackend.service.group;

import java.util.UUID;

record UpdateGroupDto(
        UUID groupId,
        UUID ownerId,
        String name,
        String description
) {
}
