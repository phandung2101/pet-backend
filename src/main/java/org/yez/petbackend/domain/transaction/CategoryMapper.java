package org.yez.petbackend.domain.transaction;

import org.yez.petbackend.repository.category.CategoryEntity;

import java.util.Set;
import java.util.stream.Collectors;

public record CategoryMapper() {

    public static Category mapperFromEntity(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName()
        );
    }

    public static Set<Category> mapperFromEntities(Set<CategoryEntity> entities) {
        return entities.stream().map(CategoryMapper::mapperFromEntity).collect(Collectors.toSet());
    }

    public static CategoryEntity mapperToEntity(Category domain) {
        var entity = new CategoryEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        return entity;
    }

    public static Set<CategoryEntity> mapperToEntities(Set<Category> domains) {
        return domains.stream().map(CategoryMapper::mapperToEntity).collect(Collectors.toSet());
    }
}
