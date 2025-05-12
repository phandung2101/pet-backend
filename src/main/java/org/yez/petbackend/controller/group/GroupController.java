package org.yez.petbackend.controller.group;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.group.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
class GroupController {

    private final GroupService groupService;

    GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupResponse> getAll(@AuthenticationPrincipal PetUser user) {
        return groupService.getAll(user.id()).stream()
                .map(group -> new GroupResponse(
                        group.getName(),
                        group.getDescription(),
                        group.getMembers().stream().map(UserEntity::getUsername).toList(),
                        group.getOwner().getUsername()
                ))
                .toList();
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    public GroupResponse getGroupById(@PathVariable String groupId) {
        var group = groupService.getById(UUID.fromString(groupId));
        return new GroupResponse(
                group.getName(),
                group.getDescription(),
                group.getMembers().stream().map(UserEntity::getUsername).toList(),
                group.getOwner().getUsername()
        );
    }

    @PostMapping
    public GroupCreateResponse create(@RequestBody CreateGroupRequest req,
                                      @AuthenticationPrincipal PetUser user) {
        var group = groupService.create(req, user.id());
        return new GroupCreateResponse(group.getName(), group.getDescription());
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void updateGroup(@PathVariable String groupId,
                            @RequestBody GroupUpdateRequestDto request) {
        groupService.updateGroup(UUID.fromString(groupId), request);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void deleteGroup(@PathVariable String groupId) {
        groupService.deleteGroup(UUID.fromString(groupId));
    }

    @PostMapping("/{groupId}/members")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void addMember(@PathVariable String groupId, @RequestBody AddMemberGroupRequest addMemberGroupRequest) {
        groupService.addMember(UUID.fromString(groupId), addMemberGroupRequest.userId());
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void removeMember(@PathVariable String groupId, @PathVariable String userId) {
        groupService.removeMember(UUID.fromString(groupId), UUID.fromString(userId));
    }

    @ExceptionHandler(GroupNotExistedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleGroupNotExisted() {
    }

    @ExceptionHandler(NotGroupOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleNotGroupOwner() {
    }
}
