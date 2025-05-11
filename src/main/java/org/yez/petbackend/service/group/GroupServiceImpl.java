package org.yez.petbackend.service.group;

import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.group.GroupRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;

import java.util.List;

@Service
public record GroupServiceImpl(
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
    public GroupEntity createDefault(final UserEntity owner) {
        final var groupEntity = new GroupEntity();
        groupEntity.setName("%s's personal group".formatted(owner.getUsername()));
        groupEntity.setDescription("This is personal group");
        groupEntity.setOwner(owner);
        groupEntity.setMembers(List.of(owner));

        return groupRepository.save(groupEntity);
    }
}
