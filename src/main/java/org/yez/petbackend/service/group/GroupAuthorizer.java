package org.yez.petbackend.service.group;

public interface GroupAuthorizer {
    boolean isOwner(String groupId);

    boolean isMember(String groupId);
}
