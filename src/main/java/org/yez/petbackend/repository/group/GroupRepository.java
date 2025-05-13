package org.yez.petbackend.repository.group;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends CrudRepository<GroupEntity, UUID> {
    Optional<GroupEntity> findByIdAndOwner_Id(UUID id, UUID ownerId);

    List<GroupEntity> findAllByMembers_Id(UUID memberId);

    void deleteByIdAndOwner_Id(UUID id, UUID ownerId);
}
