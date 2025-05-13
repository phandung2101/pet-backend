package org.yez.petbackend.domain.transaction;

import org.yez.petbackend.repository.category.CategoryEntity;

import java.util.UUID;

public record Category(
        UUID id,
        String name
) {

    public Category(CategoryEntity entity) {
        this(
                entity.getId(),
                entity.getName()
        );
    }

    CategoryEntity toEntity() {
        var entity = new CategoryEntity();
        entity.setId(this.id());
        entity.setName(this.name());
        return entity;
    }
}
