package org.yez.petbackend.domain.transaction;

import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record Transaction(
        UUID id,
        UUID creatorId,
        UUID groupId,
        Instant transactionTime,
        double amount,
        String description,
        TransactionType type,
        Set<Category> categories,
        Instant createdAt,
        Instant updatedAt
) {

}
