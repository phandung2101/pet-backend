package org.yez.petbackend.repository.category;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends CrudRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);
}
