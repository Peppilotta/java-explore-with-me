package ru.practicum.ewm.admin;

import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.category.NewCategoryDto;

public interface CategoryServiceAdmin {

    CategoryDto addCategory(NewCategoryDto category);

    CategoryDto editCategory(Long catId, NewCategoryDto category);

    CategoryDto deleteCategory(Long catId);
}