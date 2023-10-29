package ru.practicum.ewm.pub;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(Long catId);
}