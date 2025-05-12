package org.yez.petbackend.service.group;

import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.group.GroupRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.service.auth.UserNotExistedException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
record GroupServiceImpl(
        UserRepository userRepository,
        GroupRepository groupRepository
) implements GroupService {

    public GroupEntity create(final CreateGroupRequest createGroupRequest, final UUID ownerId) {
        final var groupEntity = new GroupEntity();
        final var userEntity = new UserEntity(ownerId);
        groupEntity.setName(createGroupRequest.name());
        groupEntity.setDescription(createGroupRequest.description());
        groupEntity.setOwner(userEntity);
        groupEntity.setMembers(Set.of(userEntity));

        return groupRepository.save(groupEntity);
    }

    @Override
    public GroupEntity getById(final UUID groupId) {
        return groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
    }

    @Override
    public List<GroupEntity> getAll(final UUID userId) {
        return groupRepository.findAllByMembers_Id(userId);
    }

    @Override
    public void createDefault(final UserEntity owner) {
        final var groupEntity = new GroupEntity();
        groupEntity.setName("%s's personal group".formatted(owner.getUsername()));
        groupEntity.setDescription("This is personal group");
        groupEntity.setOwner(owner);
        groupEntity.setMembers(Set.of(owner));

        groupRepository.save(groupEntity);
    }

    @Override
    public void addMember(final UUID groupId, final UUID memberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().anyMatch(mem -> mem.getId().equals(memberId))) {
            return;
        }
        final var newMember = userRepository.findById(memberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().add(newMember);
        groupRepository.save(group);
    }

    @Override
    public void removeMember(final UUID groupId, final UUID memberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().noneMatch(mem -> mem.getId().equals(memberId))) {
            return;
        }
        final var removeMember = userRepository.findById(memberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().removeIf(mem -> mem.getId().equals(removeMember.getId()));
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(final UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public void updateGroup(final UUID groupId, final GroupUpdateRequestDto groupUpdateRequestDto) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        group.setName(groupUpdateRequestDto.name());
        group.setDescription(groupUpdateRequestDto.description());
        groupRepository.save(group);
    }
}
