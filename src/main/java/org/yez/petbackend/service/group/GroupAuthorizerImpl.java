package org.yez.petbackend.service.group;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.group.GroupRepository;
import org.yez.petbackend.security.PetUser;

import java.util.UUID;

@Service("groupAuthorizer")
record GroupAuthorizerImpl(
        GroupRepository groupRepository
) implements GroupAuthorizer {

    @Override
    public boolean isOwner(String groupId) {
        var currentUser = (PetUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var group = groupRepository.findById(UUID.fromString(groupId)).orElseThrow(NotGroupOwnerException::new);
        return group.getOwner().getId().equals(currentUser.getId());
    }

    @Override
    public boolean isMember(String groupId) {
        var currentUser = (PetUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var group = groupRepository.findById(UUID.fromString(groupId)).orElseThrow(NotGroupOwnerException::new);
        return group.getMembers().stream().anyMatch(it -> it.getId().equals(currentUser.getId()));
    }
}
