package ru.practicum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.services.interfaces.CategoryServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/admin/categories")
@Validated
public class CategoriesControllerAdmin {

    private final CategoryServiceAdmin categoryServiceAdmin;

    public CategoriesControllerAdmin(CategoryServiceAdmin categoryServiceAdmin) {
        this.categoryServiceAdmin = categoryServiceAdmin;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid final NewCategoryDto category) {
        return categoryServiceAdmin.addCategory(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryDto deleteCategory(@PathVariable @Positive long catId) {
        return categoryServiceAdmin.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto deleteCategory(@PathVariable @Positive long catId,
                                      @RequestBody @Valid final NewCategoryDto category) {
        return categoryServiceAdmin.editCategory(catId, category);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable @Positive long catId) {
        return categoryServiceAdmin.getCategory(catId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories() {
        return categoryServiceAdmin.getCategories();
    }
}