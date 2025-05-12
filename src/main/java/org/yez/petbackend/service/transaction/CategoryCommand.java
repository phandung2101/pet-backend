package org.yez.petbackend.service.transaction;

import org.yez.petbackend.domain.transaction.Category;

interface CategoryCommand {
    Category findAndInsert(String name);
}
