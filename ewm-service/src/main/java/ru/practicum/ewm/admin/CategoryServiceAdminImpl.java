package ru.practicum.ewm.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryRepository;
import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.error.ErrorStatus;
import ru.practicum.ewm.exception.ItemDoesNotExistException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public CategoryDto addCategory(NewCategoryDto newCategory) {
        log.info("Request for creating Category = {}", newCategory);
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toCategory(newCategory)));
    }

    public CategoryDto editCategory(Long catId, NewCategoryDto category) {
        log.info("Request for updating Category with id = {}", catId);
        checkCategoryExists(catId);
        Category updatedCategory = categoryRepository.findById(catId).get();
        updatedCategory.setName(category.getName());
        log.info("Category updated. {}", updatedCategory);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    public CategoryDto deleteCategory(Long catId) {
        log.info("Request for deleting Category with id = {}", catId);
        checkCategoryExists(catId);
        Category deletedCategory = categoryRepository.findById(catId).get();
        categoryRepository.deleteById(catId);
        log.info("Category deleted. {}", deletedCategory);
        return categoryMapper.toDto(deletedCategory);
    }

    private void checkCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            String message = "Category with id=" + catId +" not exists";
            ApiError apiError = ApiError.builder()
                    .message(message)
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new ItemDoesNotExistException(message);
        }
    }
}