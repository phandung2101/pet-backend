package org.yez.petbackend.service.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.group.Group;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.group.GroupRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.service.auth.UserNotExistedException;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class GroupCommandImpl implements GroupCommand {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public Group create(final CreateGroupDto createGroupDto) {
        final var groupEntity = new GroupEntity();
        final var userEntity = new UserEntity(createGroupDto.ownerId());
        groupEntity.setName(createGroupDto.name());
        groupEntity.setDescription(createGroupDto.description());
        groupEntity.setOwner(userEntity);
        groupEntity.setMembers(Set.of(userEntity));

        groupRepository.save(groupEntity);
        return new Group(groupEntity);
    }

    @Override
    public void update(final UpdateGroupDto updateGroupDto) {
        final var group = groupRepository.findByIdAndOwner_Id(updateGroupDto.groupId(), updateGroupDto.ownerId())
                .orElseThrow(GroupNotExistedException::new);
        group.setName(updateGroupDto.name());
        group.setDescription(updateGroupDto.description());

        groupRepository.save(group);
    }

    @Override
    public void delete(final UUID ownerId, final UUID groupId) {
        groupRepository.deleteByIdAndOwner_Id(groupId, ownerId);
    }

    @Override
    public void addMember(final UUID groupId, final UUID newMemberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().anyMatch(mem -> mem.getId().equals(newMemberId))) {
            return;
        }
        final var newMember = userRepository.findById(newMemberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().add(newMember);
        groupRepository.save(group);
    }

    @Override
    public void removeMember(final UUID groupId, final UUID removeMemberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().noneMatch(mem -> mem.getId().equals(removeMemberId))) {
            return;
        }
        final var removeMember = userRepository.findById(removeMemberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().removeIf(mem -> mem.getId().equals(removeMember.getId()));
        groupRepository.save(group);
    }
}
