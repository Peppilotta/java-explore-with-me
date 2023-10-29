package ru.practicum.ewm.admin;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.category.NewCategoryDto;

@Service
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    public CategoryDto addCategory(NewCategoryDto category) {
        return new CategoryDto();
    }

    public CategoryDto editCategory(Long catId, NewCategoryDto category) {
        return new CategoryDto();
    }

    public CategoryDto deleteCategory(Long catId) {
        return new CategoryDto();
    }
}