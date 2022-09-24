package ru.andrianov.emw.business.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Category addNewCategory(@RequestBody @Valid Category category) {
        log.info("(Admin)CategoryController.addNewCategory: received request to add new category {}", category.getName());
        return categoryService.addNewCategory(category);
    }

    @PatchMapping
    public Category updateCategory(@RequestBody @Valid Category category) {
        log.info("(Admin)CategoryController.updateCategory: received request to update category with id={}", category.getId());
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    private void deleteCategory(@PathVariable Long categoryId) {
        log.info("(Admin)CategoryController.deleteCategory: received request to delete category with id={}", categoryId);
        categoryService.deleteCategoryById(categoryId);
    }

}
