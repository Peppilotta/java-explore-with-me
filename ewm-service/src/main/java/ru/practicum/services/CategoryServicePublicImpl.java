package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.exception.NotFoundException;
import ru.practicum.services.interfaces.CategoryServicePublic;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServicePublicImpl implements CategoryServicePublic {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        log.info("Get Categories Public");
        return categoryRepository.findAllPageable(pageable).getContent();
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        log.info("Get Category Public with id={}", catId);
        checkCategoryExists(catId);
        return categoryMapper.toDto(categoryRepository.findById(catId).orElseGet(Category::new));
    }

    private void checkCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            ApiError apiError = new ApiError(ErrorStatus.E_404_NOT_FOUND.getValue(),
                    "The required object was not found.",
                    "Category with id=" + catId + "  was not found",
                    LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }
}