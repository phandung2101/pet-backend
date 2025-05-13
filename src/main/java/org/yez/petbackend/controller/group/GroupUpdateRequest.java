package org.yez.petbackend.controller.group;

public record GroupUpdateRequest(
        String name,
        String description
) {
}
