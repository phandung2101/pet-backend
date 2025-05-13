package org.yez.petbackend.service.transaction;

import org.yez.petbackend.controller.transaction.CreateTransactionRequest;
import org.yez.petbackend.controller.transaction.UpdateTransactionRequest;
import org.yez.petbackend.domain.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction create(
            UUID groupId,
            UUID userId,
            CreateTransactionRequest createTransactionRequest
    );

    List<Transaction> findAll(UUID groupId);

    Transaction findOne(UUID groupId, UUID transactionId);

    void update(
            UUID groupId,
            final UUID transactionId, UpdateTransactionRequest updateTransactionRequest
    );

    void delete(
            UUID groupId,
            UUID userId,
            UUID transactionId
    );


}
