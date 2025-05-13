package org.yez.petbackend.service.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yez.petbackend.controller.group.CreateGroupRequest;
import org.yez.petbackend.controller.group.GroupUpdateRequest;
import org.yez.petbackend.domain.group.Group;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
final class GroupServiceImpl implements GroupService {
    private final GroupCommand groupCommand;
    private final GroupQuery groupQuery;

    public Group create(final CreateGroupRequest createGroupRequest, final UUID userId) {
        return groupCommand.create(new CreateGroupDto(
                userId,
                createGroupRequest.name(),
                createGroupRequest.description()
        ));
    }

    @Override
    public Group getById(final UUID userId, final UUID groupId) {
        return groupQuery.findOne(userId, groupId);
    }

    @Override
    public List<Group> getAll(final UUID userId) {
        return groupQuery.findAll(userId);
    }

    @Override
    public void createDefault(final UUID ownerId, final String username) {
        final var createGroupDto = new CreateGroupDto(
                ownerId,
                "%s's personal group".formatted(username),
                "This is personal group"
        );

        groupCommand.create(createGroupDto);
    }

    @Override
    public void addMember(final UUID groupId, final UUID memberId) {
        groupCommand.addMember(groupId, memberId);
    }

    @Override
    public void removeMember(final UUID groupId, final UUID memberId) {
        groupCommand.removeMember(groupId, memberId);
    }

    @Override
    public void deleteGroup(final UUID ownerId, final UUID groupId) {
        groupCommand.delete(ownerId, groupId);
    }

    @Override
    public void updateGroup(final UUID ownerId, final UUID groupId, final GroupUpdateRequest groupUpdateRequest) {
        groupCommand.update(new UpdateGroupDto(
                groupId,
                ownerId,
                groupUpdateRequest.name(),
                groupUpdateRequest.description()
        ));
    }

}
