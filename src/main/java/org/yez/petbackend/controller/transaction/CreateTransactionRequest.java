package org.yez.petbackend.controller.transaction;

import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.Set;

public record CreateTransactionRequest(
        float amount,
        String description,
        TransactionType type,
        Instant transactionTime,
        Set<String> categories
) {
}
