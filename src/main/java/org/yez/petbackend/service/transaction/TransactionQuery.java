package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionQuery {
    Transaction findOne(UUID groupId, UUID transactionId);

    List<Transaction> findAll(UUID groupId);
}
