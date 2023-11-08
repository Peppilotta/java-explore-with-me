package ru.practicum.admin;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public interface CategoryServiceAdmin {

    CategoryDto addCategory(NewCategoryDto category);

    CategoryDto editCategory(Long catId, NewCategoryDto category);

    CategoryDto deleteCategory(Long catId);
}