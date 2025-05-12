package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.Transaction;

import java.util.UUID;

public interface TransactionCommand {

    Transaction create(final CreateTransactionDto createTransactionDto);

    void modify(final ModifyTransactionDto modifyTransactionDto);

    void delete(final UUID groupId, final UUID transactionId);
}
