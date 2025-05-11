package org.yez.petbackend.service.transaction;

import org.yez.petbackend.repository.transaction.TransactionEntity;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionEntity create(
            UUID groupId,
            UUID userId,
            TransactionCreateRequestDto transactionCreateRequestDto
    );

    List<TransactionEntity> getAll(UUID groupId);

    TransactionEntity getOne(UUID groupId, UUID transactionId);

    void update(
            UUID groupId,
            TransactionUpdateRequestDto transactionUpdateRequestDto
    );

    void delete(
            UUID groupId,
            UUID userId,
            UUID transactionId
    );


}
