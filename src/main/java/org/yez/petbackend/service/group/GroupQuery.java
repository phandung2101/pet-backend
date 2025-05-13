package org.yez.petbackend.service.group;

import org.yez.petbackend.domain.group.Group;

import java.util.List;
import java.util.UUID;

interface GroupQuery {

    List<Group> findAll(UUID userId);

    Group findOne(UUID userId, UUID groupId);
}
