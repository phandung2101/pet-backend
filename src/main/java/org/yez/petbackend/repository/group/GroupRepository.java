package org.yez.petbackend.repository.group;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GroupRepository extends CrudRepository<GroupEntity, UUID> {
}
