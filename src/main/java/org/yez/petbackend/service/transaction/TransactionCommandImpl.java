package org.yez.petbackend.service.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.transaction.Transaction;
import org.yez.petbackend.domain.transaction.TransactionMapper;
import org.yez.petbackend.repository.category.CategoryEntity;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.transaction.TransactionEntity;
import org.yez.petbackend.repository.transaction.TransactionRepository;
import org.yez.petbackend.repository.user.UserEntity;

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
        entity.setType(createTransactionDto.type());
        entity.setCategories(createTransactionDto.categoryIds().stream().map(CategoryEntity::new).collect(Collectors.toSet()));
        entity.setDescription(createTransactionDto.description());

        transactionRepository.save(entity);
        return TransactionMapper.mapFromEntity(entity);
    }

    @Override
    public void modify(
            final ModifyTransactionDto modifyTransactionDto
    ) {
        final var entity = transactionRepository.findByGroup_IdAndId(
                modifyTransactionDto.groupId(),
                modifyTransactionDto.transactionId()
        ).orElseThrow(TransactionNotExistedException::new);
        var isUpdate = false;
        if (entity.getAmount() != modifyTransactionDto.amount()) {
            entity.setAmount(modifyTransactionDto.amount());
            isUpdate = true;
        }
        if (!entity.getDescription().equals(modifyTransactionDto.description())) {
            entity.setDescription(modifyTransactionDto.description());
            isUpdate = true;
        }
        if (entity.getAmount() != modifyTransactionDto.amount()) {
            entity.setAmount(modifyTransactionDto.amount());
            isUpdate = true;
        }
        if (isUpdate) transactionRepository.save(entity);
    }

    @Override
    public void delete(final UUID groupId, final UUID transactionId) {
        transactionRepository.deleteByGroup_IdAndId(groupId, transactionId);
    }


}
