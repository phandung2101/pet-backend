package org.yez.petbackend.domain.transaction;

import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.transaction.TransactionEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.time.Instant;

public record TransactionMapper() {

    public static Transaction mapFromEntity(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .creatorId(entity.getCreator().getId())
                .groupId(entity.getGroup().getId())
                .transactionTime(entity.getTransactionTime())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .type(entity.getType())
                .categories(CategoryMapper.mapperFromEntities(entity.getCategories()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TransactionEntity mapToEntity(
            Transaction domain
    ) {
        var entity = new TransactionEntity();
        entity.setId(domain.id());
        entity.setCreator(new UserEntity(domain.creatorId()));
        entity.setGroup(new GroupEntity(domain.groupId()));
        entity.setTransactionTime(domain.transactionTime());
        entity.setAmount(domain.amount());
        entity.setDescription(domain.description());
        entity.setType(domain.type());
        entity.setCategories(CategoryMapper.mapperToEntities(domain.categories()));
        entity.setCreatedAt(domain.createdAt() != null ? domain.createdAt() : Instant.now());
        entity.setUpdatedAt(domain.updatedAt() != null ? domain.updatedAt() : Instant.now());
        return entity;
    }

}
