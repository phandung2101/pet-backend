package org.yez.petbackend.service.group;

import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;

public interface GroupService {

    GroupEntity create(GroupCreateRequestDto groupCreateRequestDto, UserEntity owner);

    GroupEntity createDefault(UserEntity owner);
}
