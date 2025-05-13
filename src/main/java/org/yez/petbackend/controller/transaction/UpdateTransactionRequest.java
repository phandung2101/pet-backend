package org.yez.petbackend.controller.transaction;

import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;

public record UpdateTransactionRequest(
        double amount,
        String description,
        TransactionType type,
        Instant transactionTime
) {
}
