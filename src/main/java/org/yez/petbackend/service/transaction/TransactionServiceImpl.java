package org.yez.petbackend.service.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yez.petbackend.controller.transaction.CreateTransactionRequest;
import org.yez.petbackend.controller.transaction.UpdateTransactionRequest;
import org.yez.petbackend.domain.transaction.Category;
import org.yez.petbackend.domain.transaction.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class TransactionServiceImpl implements TransactionService {
    private final TransactionCommand transactionCommand;
    private final TransactionQuery transactionQuery;
    private final CategoryCommand categoryCommand;

    @Override
    @Transactional
    public Transaction create(
            final UUID groupId,
            final UUID userId,
            final CreateTransactionRequest createTransactionRequest
    ) {
        final var categoryIds = createTransactionRequest.categories()
                .stream()
                .map(categoryCommand::findAndInsert)
                .map(Category::id)
                .collect(Collectors.toSet());

        return transactionCommand.create(new CreateTransactionDto(
                userId,
                groupId,
                createTransactionRequest.amount(),
                createTransactionRequest.description(),
                createTransactionRequest.type(),
                createTransactionRequest.transactionTime(),
                categoryIds
        ));
    }

    @Override
    public List<Transaction> findAll(final UUID groupId) {
        return transactionQuery.findAll(groupId);
    }

    @Override
    public Transaction findOne(final UUID groupId, final UUID transactionId) {
        return transactionQuery.findOne(groupId, transactionId);
    }

    @Override
    public void update(
            final UUID groupId,
            final UUID transactionId,
            final UpdateTransactionRequest updateTransactionRequest
    ) {
        transactionCommand.update(new UpdateTransactionDto(
                groupId,
                transactionId,
                updateTransactionRequest.amount(),
                updateTransactionRequest.description(),
                updateTransactionRequest.type(),
                updateTransactionRequest.transactionTime()
        ));
    }

    @Override
    public void delete(final UUID groupId, final UUID userId, final UUID transactionId) {
        transactionCommand.delete(groupId, transactionId);
    }
}
