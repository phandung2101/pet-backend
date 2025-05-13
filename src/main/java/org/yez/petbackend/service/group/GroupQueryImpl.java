package org.yez.petbackend.service.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.group.Group;
import org.yez.petbackend.repository.group.GroupRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class GroupQueryImpl implements GroupQuery {
    private final GroupRepository groupRepository;

    @Override
    public List<Group> findAll(final UUID userId) {
        return groupRepository.findAllByMembers_Id(userId).stream()
                .map(Group::new)
                .toList();
    }

    @Override
    public Group findOne(final UUID userId, final UUID groupId) {
        return groupRepository.findById(groupId)
                .filter(group -> group.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(userId)))
                .map(Group::new)
                .orElseThrow(GroupNotExistedException::new);
    }
}
