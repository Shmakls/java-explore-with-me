package ru.andrianov.emw.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.business.service.PublicApiService;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.service.CategoryService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    private final PublicApiService publicApiService;

    @PostMapping("/admin/categories")
    public Category addNewCategory(@RequestBody @Valid Category category) {
        log.info("(Admin)CategoryController.addNewCategory: received request to add new category {}", category.getName());
        return categoryService.addNewCategory(category);
    }

    @PatchMapping("/admin/categories")
    public Category updateCategory(@RequestBody @Valid Category category) {
        log.info("(Admin)CategoryController.updateCategory: received request to update category with id={}", category.getId());
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    private void deleteCategory(@PathVariable Long categoryId) {
        log.info("(Admin)CategoryController.deleteCategory: received request to delete category with id={}", categoryId);
        categoryService.deleteCategoryById(categoryId);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Public)CategoryController.getAllCategories: received a request to get all categories");

        return publicApiService.getAllCategoriesByPages(from, size);

    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {

        log.info("(Public)CategoryController.getCategoryById: received a request to get category with id={}", categoryId);

        return publicApiService.getCategoryById(categoryId);

    }

}
