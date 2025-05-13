package org.yez.petbackend.service.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yez.petbackend.domain.transaction.Transaction;
import org.yez.petbackend.domain.transaction.TransactionType;
import org.yez.petbackend.repository.category.CategoryEntity;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.transaction.TransactionEntity;
import org.yez.petbackend.repository.transaction.TransactionRepository;
import org.yez.petbackend.repository.user.UserEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class TransactionCommandImpl implements TransactionCommand {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction create(final CreateTransactionDto createTransactionDto) {
        final var entity = new TransactionEntity();

        entity.setCreator(new UserEntity(createTransactionDto.creatorId()));
        entity.setGroup(new GroupEntity(createTransactionDto.groupId()));
        entity.setAmount(createTransactionDto.amount());
        entity.setDescription(createTransactionDto.description());
        entity.setType(Optional.ofNullable(createTransactionDto.type()).orElse(TransactionType.EXPENSE));
        entity.setCategories(Optional.ofNullable(createTransactionDto.categoryIds()).orElse(Collections.emptySet())
                .stream().map(CategoryEntity::new).collect(Collectors.toSet()));
        entity.setDescription(createTransactionDto.description());
        entity.setTransactionTime(Optional.ofNullable(createTransactionDto.transactionTime()).orElse(Instant.now()));

        transactionRepository.save(entity);
        return new Transaction(entity);
    }

    @Override
    public void update(final UpdateTransactionDto updateTransactionDto) {
        final var entity = transactionRepository.findByGroup_IdAndId(
                updateTransactionDto.groupId(),
                updateTransactionDto.transactionId()
        ).orElseThrow(TransactionNotExistedException::new);
        var isUpdate = false;
        if (entity.getAmount() != updateTransactionDto.amount()) {
            entity.setAmount(updateTransactionDto.amount());
            isUpdate = true;
        }
        if (!entity.getDescription().equals(updateTransactionDto.description())) {
            entity.setDescription(updateTransactionDto.description());
            isUpdate = true;
        }
        if (entity.getAmount() != updateTransactionDto.amount()) {
            entity.setAmount(updateTransactionDto.amount());
            isUpdate = true;
        }
        if (isUpdate) transactionRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(final UUID groupId, final UUID transactionId) {
        transactionRepository.deleteByGroup_IdAndId(groupId, transactionId);
    }


}
