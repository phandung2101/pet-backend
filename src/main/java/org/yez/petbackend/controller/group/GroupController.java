package org.yez.petbackend.controller.group;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.domain.group.GroupMember;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.auth.UserNotExistedException;
import org.yez.petbackend.service.group.GroupNotExistedException;
import org.yez.petbackend.service.group.GroupService;
import org.yez.petbackend.service.group.NotGroupOwnerException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
class GroupController {
    private final GroupService groupService;

    @PostMapping
    public GroupCreateResponse create(@RequestBody CreateGroupRequest req,
                                      @AuthenticationPrincipal PetUser user) {
        var group = groupService.create(req, user.getId());
        return new GroupCreateResponse(group.name(), group.description());
    }


    @GetMapping
    public List<GroupResponse> findAll(@AuthenticationPrincipal PetUser user) {
        return groupService.findAll(user.getId()).stream()
                .map(group -> new GroupResponse(
                        group.id().toString(),
                        group.name(),
                        group.description(),
                        group.members().stream().map(GroupMember::username).toList(),
                        group.owner().username()
                ))
                .toList();
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    public GroupResponse findOne(@AuthenticationPrincipal PetUser user, @PathVariable String groupId) {
        var group = groupService.findOne(user.getId(), UUID.fromString(groupId));
        return new GroupResponse(
                group.id().toString(),
                group.name(),
                group.description(),
                group.members().stream().map(GroupMember::username).toList(),
                group.owner().username()
        );
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void updateGroup(
            @AuthenticationPrincipal PetUser user,
            @PathVariable String groupId,
            @RequestBody GroupUpdateRequest request
    ) {
        groupService.updateGroup(user.getId(), UUID.fromString(groupId), request);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("@groupAuthorizer.isOwner(#groupId)")
    public void deleteGroup(@AuthenticationPrincipal PetUser user, @PathVariable String groupId) {
        groupService.deleteGroup(user.getId(), UUID.fromString(groupId));
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

    @ExceptionHandler(UserNotExistedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleAddOrRemoveUser() {
    }

    @ExceptionHandler(NotGroupOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleNotGroupOwner() {
    }
}
