package org.yez.petbackend.service.transaction;

import org.yez.petbackend.repository.transaction.TransactionType;

import java.time.Instant;

public record TransactionCreateRequestDto(
        float amount,
        String description,
        TransactionType type,
        String category,
        Instant transactionTime
) {
}
