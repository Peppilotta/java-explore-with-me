package ru.practicum.ewm.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById(@Nullable Long id);

}