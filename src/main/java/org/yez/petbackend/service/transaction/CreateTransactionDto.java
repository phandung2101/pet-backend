package org.yez.petbackend.service.transaction;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record CreateTransactionDto(
        UUID creatorId,
        UUID groupId,
        float amount,
        String description,
        @Nullable TransactionType type,
        @Nullable Instant transactionTime,
        @Nullable Set<UUID> categoryIds
) {
}
