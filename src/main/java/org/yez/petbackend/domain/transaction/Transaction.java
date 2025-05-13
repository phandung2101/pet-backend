package org.yez.petbackend.domain.transaction;

import lombok.Builder;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.transaction.TransactionEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record Transaction(
        UUID id,
        UUID creatorId,
        UUID groupId,
        Instant transactionTime,
        double amount,
        String description,
        TransactionType type,
        Set<Category> categories,
        Instant createdAt,
        Instant updatedAt
) {

    TransactionEntity toEntity() {
        var entity = new TransactionEntity();
        entity.setId(this.id());
        entity.setCreator(new UserEntity(this.creatorId()));
        entity.setGroup(new GroupEntity(this.groupId()));
        entity.setTransactionTime(this.transactionTime());
        entity.setAmount(this.amount());
        entity.setDescription(this.description());
        entity.setType(this.type());
        entity.setCategories(this.categories().stream().map(Category::toEntity).collect(Collectors.toSet()));
        entity.setCreatedAt(this.createdAt() != null ? this.createdAt() : Instant.now());
        entity.setUpdatedAt(this.updatedAt() != null ? this.updatedAt() : Instant.now());
        return entity;
    }

    public Transaction(TransactionEntity entity) {
        this(
                entity.getId(),
                entity.getCreator().getId(),
                entity.getGroup().getId(),
                entity.getTransactionTime(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getType(),
                entity.getCategories().stream().map(Category::new).collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
