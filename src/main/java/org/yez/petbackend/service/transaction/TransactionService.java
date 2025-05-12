package org.yez.petbackend.service.transaction;

import org.yez.petbackend.controller.transaction.CreateTransactionRequest;
import org.yez.petbackend.controller.transaction.ModifyTransactionRequest;
import org.yez.petbackend.domain.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction create(
            UUID groupId,
            UUID userId,
            CreateTransactionRequest createTransactionRequest
    );

    List<Transaction> getAll(UUID groupId);

    Transaction getOne(UUID groupId, UUID transactionId);

    void update(
            UUID groupId,
            final UUID transactionId, ModifyTransactionRequest modifyTransactionRequest
    );

    void delete(
            UUID groupId,
            UUID userId,
            UUID transactionId
    );


}
