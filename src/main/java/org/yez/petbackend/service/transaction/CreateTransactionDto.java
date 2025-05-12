package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record CreateTransactionDto(
        UUID creatorId,
        UUID groupId,
        float amount,
        String description,
        TransactionType type,
        Instant transactionTime,
        Set<UUID> categoryIds
) {
}
