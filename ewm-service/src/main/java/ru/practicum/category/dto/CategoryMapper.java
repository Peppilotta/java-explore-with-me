package ru.practicum.category.dto;

import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;

@Component
public class CategoryMapper {

    public Category toCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}