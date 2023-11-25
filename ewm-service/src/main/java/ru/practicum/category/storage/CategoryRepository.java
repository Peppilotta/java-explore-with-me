package ru.practicum.category.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

 //   boolean existsById(@Nullable Long id);

    @Query("select new ru.practicum.category.dto.CategoryDto(c.id, c.name) from Category as c order by c.id")
    Page<CategoryDto> findAllPageable(Pageable pageable);

    @Query("select c from Category as c order by c.id")
    List<Category> findAllOrderById();

    boolean existsByName(String name);

    Category findByName(String name);

    @Modifying
    @Query("delete from Category as c where c.id = :id")
    void deleteById(@Param("id") @Nullable Long id);
}