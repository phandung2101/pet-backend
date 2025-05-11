package org.yez.petbackend.service.group;

public record GroupUpdateRequestDto(
        String name,
        String description
) {
}
