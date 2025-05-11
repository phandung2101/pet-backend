package org.yez.petbackend.service.group;

import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.UUID;

public interface GroupService {

    GroupEntity create(GroupCreateRequestDto groupCreateRequestDto, UserEntity owner);

    GroupEntity getById(UUID groupId);

    void createDefault(UserEntity owner);

    void addMember(UUID groupId, UUID memberId);

    void removeMember(UUID groupId, UUID memberId);

    void deleteGroup(UUID groupId);

    void updateGroup(UUID groupId, GroupUpdateRequestDto groupUpdateRequestDto);
}
