package org.yez.petbackend.controller.transaction;

import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;

public record ModifyTransactionRequest(
        float amount,
        String description,
        TransactionType type,
        Instant transactionTime
) {
}
