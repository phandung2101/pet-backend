package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.UUID;

record UpdateTransactionDto(
        UUID transactionId,
        UUID groupId,
        double amount,
        String description,
        TransactionType type,
        Instant transactionTime
) {
}
