package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.Transaction;

import java.util.UUID;

interface TransactionCommand {

    Transaction create(final CreateTransactionDto createTransactionDto);

    void update(final UpdateTransactionDto updateTransactionDto);

    void delete(final UUID groupId, final UUID transactionId);
}
