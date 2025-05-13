package org.yez.petbackend.service.group;

import org.yez.petbackend.domain.group.Group;

import java.util.UUID;

interface GroupCommand {

    Group create(CreateGroupDto createGroupDto);

    void update(UpdateGroupDto updateGroupDto);

    void delete(final UUID ownerId, UUID groupId);

    void addMember(UUID groupId, UUID newMemberId);

    void removeMember(UUID groupId, UUID removeMemberId);
}
