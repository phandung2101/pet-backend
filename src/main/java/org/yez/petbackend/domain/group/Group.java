package org.yez.petbackend.domain.group;

import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Group(
        UUID id,
        Set<GroupMember> members,
        GroupMember owner,
        String name,
        String description
) {

    public Group(GroupEntity entity) {
        this(
                entity.getId(),
                entity.getMembers().stream().map(GroupMember::new).collect(Collectors.toSet()),
                new GroupMember(entity.getOwner()),
                entity.getName(),
                entity.getDescription()
        );
    }

    GroupEntity toEntity() {
        final var entity = new GroupEntity();
        entity.setId(this.id);
        entity.setMembers(this.members().stream().map(GroupMember::toEntity).collect(Collectors.toSet()));
        entity.setOwner(owner.toEntity());
        entity.setName(this.name);
        entity.setDescription(this.description);
        return entity;
    }
}
