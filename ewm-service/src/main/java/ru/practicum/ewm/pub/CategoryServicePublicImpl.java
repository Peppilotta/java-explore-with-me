package ru.practicum.ewm.pub;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.CategoryDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServicePublicImpl implements CategoryServicePublic {

    public List<CategoryDto> getCategories(Pageable pageable) {
        return new ArrayList<>();
    }

    public CategoryDto getCategory(Long catId) {
        return new CategoryDto();
    }
}