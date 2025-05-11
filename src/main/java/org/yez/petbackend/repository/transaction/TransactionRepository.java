package org.yez.petbackend.repository.transaction;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID> {
}
