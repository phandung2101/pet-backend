package org.yez.petbackend.controller.transaction;

import org.springframework.lang.Nullable;
import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.Set;

public record CreateTransactionRequest(
        double amount,
        String description,
        @Nullable TransactionType type,
        @Nullable Instant transactionTime,
        @Nullable Set<String> categories
) {
}
