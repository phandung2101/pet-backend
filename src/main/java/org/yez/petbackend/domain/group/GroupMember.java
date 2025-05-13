package org.yez.petbackend.domain.group;

import org.yez.petbackend.repository.user.UserEntity;

import java.util.UUID;

public record GroupMember(
        UUID id,
        String username,
        String email
) {

    public GroupMember(UserEntity entity) {
        this(entity.getId(), entity.getUsername(), entity.getEmail());
    }

    public UserEntity toEntity() {
        return new UserEntity(this.id());
    }
}
