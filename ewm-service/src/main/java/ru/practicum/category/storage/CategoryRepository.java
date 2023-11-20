package ru.practicum.category.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById(@Nullable Long id);

    @Query("select c from Category as c order by c.id")
    List<Category> findAllOrderById();

    @Query("select c from Category as c")
    Page<Category> findAllPageable(@Nullable Pageable pageable);

    boolean existsByName(String name);

    Category findByName(String name);

    @Transactional
    @Modifying
    @Query("delete from Category as c where c.id = :id")
    void deleteById(Long id);
}