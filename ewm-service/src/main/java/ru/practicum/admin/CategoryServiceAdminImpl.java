package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    public CategoryDto addCategory(NewCategoryDto newCategory) {
        log.info("Request for creating Category = {}", newCategory);
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toCategory(newCategory)));
    }

    public CategoryDto editCategory(Long catId, NewCategoryDto category) {
        log.info("Request for updating Category with id = {}", catId);
        checkCategoryExists(catId);
        Category updatedCategory = categoryRepository.findById(catId).orElseGet(Category::new);
        updatedCategory.setName(category.getName());
        log.info("Category updated. {}", updatedCategory);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    public CategoryDto deleteCategory(Long catId) {
        log.info("Request for deleting Category with id = {}", catId);
        checkCategoryExists(catId);
        checkEventsOfCategoryExists(catId);
        Category deletedCategory = categoryRepository.findById(catId).orElseGet(Category::new);
        categoryRepository.deleteById(catId);
        log.info("Category deleted. {}", deletedCategory);
        return categoryMapper.toDto(deletedCategory);
    }

    private void checkCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            String message = "Category with id=" + catId + "  was not found";
            ApiError apiError = ApiError.builder()
                    .message(message)
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiError);
        }
    }

    private void checkEventsOfCategoryExists(Long catId) {
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            ApiError apiError = ApiError.builder()
                    .message("The category is not empty")
                    .reason("For the requested operation the conditions are not met.")
                    .status(ErrorStatus.E_409_CONFLICT.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new ConflictException(apiError);
        }
    }
}