package ru.practicum.services;

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
import ru.practicum.services.interfaces.CategoryServiceAdmin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategory) {
        log.info("Request for creating Category = {}", newCategory);
        checkCategoryNameUnique(newCategory.getName(), 0L);
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toCategory(newCategory)));
    }

    @Override
    public CategoryDto editCategory(Long catId, NewCategoryDto category) {
        log.info("Request for updating Category with id = {}", catId);
        checkCategoryExists(catId);
        checkCategoryNameUnique(category.getName(), catId);
        Category updatedCategory = categoryRepository.findById(catId).orElseGet(Category::new);
        updatedCategory.setName(category.getName());
        log.info("Category updated. {}", updatedCategory);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public CategoryDto deleteCategory(Long catId) {
        log.info("Request for deleting Category with id = {}", catId);
        checkCategoryExists(catId);
        checkEventsOfCategoryExists(catId);
        Category deletedCategory = categoryRepository.findById(catId).orElseGet(Category::new);
        categoryRepository.deleteById(catId);
        log.info("Category deleted. {}", deletedCategory);
        return categoryMapper.toDto(deletedCategory);
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        log.info("Request for get Category with id = {}", catId);
        checkCategoryExists(catId);
        checkEventsOfCategoryExists(catId);
        Category category = categoryRepository.findById(catId).orElseGet(Category::new);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getCategories() {
        log.info("Request for all categories");
        return categoryMapper.toDtos(categoryRepository.findAllOrderById());
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

    private void checkEventsOfCategoryExists(Long catId) {
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            ApiError apiError = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
                    "For the requested operation the conditions are not met.",
                    "The category is not empty", LocalDateTime.now());
            throw new ConflictException(apiError);
        }
    }

    private void checkCategoryNameUnique(String name, Long id) {
        ApiError apiError = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
                "For the requested operation the conditions are not met.",
                "The category with name = " + name + " exists", LocalDateTime.now());

        if (categoryRepository.existsByName(name)
                && (id == 0 || !Objects.equals((categoryRepository.findByName(name).getId()), id))) {
            throw new ConflictException(apiError);
        }
    }
}