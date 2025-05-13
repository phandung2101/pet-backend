package org.yez.petbackend.controller.transaction;

import org.yez.petbackend.domain.transaction.Category;
import org.yez.petbackend.domain.transaction.Transaction;
import org.yez.petbackend.domain.transaction.TransactionType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

record TransactionResponse(
        UUID id,
        UUID creatorId,
        Instant transactionTime,
        double amount,
        String description,
        TransactionType type,
        List<String> categories
) {

    public TransactionResponse(Transaction domain) {
        this(domain.id(),
                domain.creatorId(),
                domain.transactionTime(),
                domain.amount(),
                domain.description(),
                domain.type(),
                domain.categories().stream().map(Category::name).toList()
        );
    }
}
