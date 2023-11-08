package ru.practicum.admin;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/categories")
@Validated
public class CategoriesControllerAdmin {

    private final CategoryServiceAdmin categoryServiceAdmin;

    public CategoriesControllerAdmin(CategoryServiceAdmin categoryServiceAdmin) {
        this.categoryServiceAdmin = categoryServiceAdmin;
    }

    @PostMapping
    public CategoryDto addCategory(@RequestBody NewCategoryDto category) {
        return categoryServiceAdmin.addCategory(category);
    }

    @DeleteMapping("/{catId}")
    public CategoryDto deleteCategory(@PathVariable("catId") @Positive long catId) {
        return categoryServiceAdmin.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto deleteCategory(@PathVariable("catId") @Positive long catId,
                                      @RequestBody NewCategoryDto category) {
        return categoryServiceAdmin.editCategory(catId, category);
    }
}