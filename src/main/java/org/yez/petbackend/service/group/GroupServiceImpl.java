package org.yez.petbackend.service.group;

import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.group.GroupRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.service.auth.UserNotExistedException;

import java.util.List;
import java.util.UUID;

@Service
record GroupServiceImpl(
        UserRepository userRepository,
        GroupRepository groupRepository
) implements GroupService {

    @Override
    public GroupEntity create(final GroupCreateRequestDto groupCreateRequestDto, final UserEntity owner) {
        final var groupEntity = new GroupEntity();
        groupEntity.setName(groupCreateRequestDto.name());
        groupEntity.setDescription(groupCreateRequestDto.description());
        groupEntity.setOwner(owner);
        groupEntity.setMembers(List.of(owner));

        return groupRepository.save(groupEntity);
    }

    @Override
    public GroupEntity getById(final UUID groupId) {
        return groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
    }

    @Override
    public void createDefault(final UserEntity owner) {
        final var groupEntity = new GroupEntity();
        groupEntity.setName("%s's personal group".formatted(owner.getUsername()));
        groupEntity.setDescription("This is personal group");
        groupEntity.setOwner(owner);
        groupEntity.setMembers(List.of(owner));

        groupRepository.save(groupEntity);
    }

    @Override
    public void addMember(final UUID groupId, final UUID memberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().anyMatch(mem -> mem.getId() == memberId)) {
            return;
        }
        final var newMember = userRepository.findById(memberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().add(newMember);
        groupRepository.save(group);
    }

    @Override
    public void removeMember(final UUID groupId, final UUID memberId) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        if (group.getMembers().stream().noneMatch(mem -> mem.getId() == memberId)) {
            return;
        }
        final var removeMember = userRepository.findById(memberId).orElseThrow(UserNotExistedException::new);
        group.getMembers().removeIf(mem -> mem.getId() == removeMember.getId());
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(final UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public void updateGroup(final UUID groupId, final GroupUpdateRequestDto groupUpdateRequestDto) {
        final var group = groupRepository.findById(groupId).orElseThrow(GroupNotExistedException::new);
        group.setName(group.getName());
        group.setDescription(group.getDescription());
        groupRepository.save(group);
    }
}
