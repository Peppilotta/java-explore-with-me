package ru.practicum.services.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(Long catId);
}