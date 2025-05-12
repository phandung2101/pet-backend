package org.yez.petbackend.service.transaction;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.transaction.Category;
import org.yez.petbackend.domain.transaction.CategoryMapper;
import org.yez.petbackend.repository.category.CategoryEntity;
import org.yez.petbackend.repository.category.CategoryRepository;

@Service
@AllArgsConstructor
class CategoryCommandImpl implements CategoryCommand {
    private final CategoryRepository categoryRepository;

    @Override
    public Category findAndInsert(final String name) {
        final var entity = categoryRepository.findByName(name)
                .orElseGet(() -> {
                    try {
                        var newEntity = new CategoryEntity();
                        newEntity.setName(name);
                        return categoryRepository.save(newEntity);
                    } catch (DataIntegrityViolationException e) {
                        return categoryRepository.findByName(name).orElseThrow();
                    }
                });

        return CategoryMapper.mapperFromEntity(entity);
    }
}
