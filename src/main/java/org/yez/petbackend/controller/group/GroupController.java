package org.yez.petbackend.controller.group;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.group.GroupCreateRequestDto;
import org.yez.petbackend.service.group.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    GroupCreateResponseDto create(@RequestBody GroupCreateRequestDto req, @AuthenticationPrincipal PetUser user) {
        var group = groupService.create(req, user.user());
        return new GroupCreateResponseDto(group.getName(), group.getDescription());
    }

}
