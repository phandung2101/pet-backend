package org.yez.petbackend.service.group;

import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    GroupEntity create(CreateGroupRequest createGroupRequest, UUID ownerId);

    GroupEntity getById(UUID groupId);

    List<GroupEntity> getAll(UUID userId);

    void createDefault(UserEntity owner);

    void addMember(UUID groupId, UUID memberId);

    void removeMember(UUID groupId, UUID memberId);

    void deleteGroup(UUID groupId);

    void updateGroup(UUID groupId, GroupUpdateRequestDto groupUpdateRequestDto);
}
