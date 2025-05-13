package org.yez.petbackend.service.group;

import org.yez.petbackend.controller.group.CreateGroupRequest;
import org.yez.petbackend.controller.group.GroupUpdateRequest;
import org.yez.petbackend.domain.group.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    Group create(CreateGroupRequest createGroupRequest, UUID ownerId);

    Group getById(final UUID userId, UUID groupId);

    List<Group> getAll(UUID userId);

    void createDefault(UUID ownerId, String username);

    void addMember(UUID groupId, UUID memberId);

    void removeMember(UUID groupId, UUID memberId);

    void deleteGroup(final UUID ownerId, UUID groupId);

    void updateGroup(UUID ownerId, UUID groupId, GroupUpdateRequest groupUpdateRequest);
}
