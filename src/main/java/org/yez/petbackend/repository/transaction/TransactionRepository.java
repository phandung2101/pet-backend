package org.yez.petbackend.repository.transaction;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID> {
    Optional<TransactionEntity> findByGroup_IdAndId(UUID groupId, UUID id);

    void deleteByGroup_IdAndId(UUID groupId, UUID id);

    List<TransactionEntity> findAllByGroup_Id(UUID groupId);
}
