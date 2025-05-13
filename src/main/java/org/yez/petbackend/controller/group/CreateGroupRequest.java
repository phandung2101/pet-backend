package org.yez.petbackend.controller.group;

public record CreateGroupRequest(
        String name,
        String description
) {
}
