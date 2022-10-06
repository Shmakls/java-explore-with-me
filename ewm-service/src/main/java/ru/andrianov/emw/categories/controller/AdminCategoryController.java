package ru.andrianov.emw.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.categories.model.Create;
import ru.andrianov.emw.categories.model.Update;
import ru.andrianov.emw.categories.service.AdminCategoryService;
import ru.andrianov.emw.categories.dto.CategoryDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;


    @PostMapping
    public CategoryDto addNewCategory(@RequestBody @Validated(Create.class) CategoryDto categoryDto) {
        log.info("(Admin)CategoryController.addNewCategory: received request to add new category {}", categoryDto.getName());
        return adminCategoryService.addNewCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        log.info("(Admin)CategoryController.updateCategory: received request to update category with id={}", categoryDto.getId());
        return adminCategoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    private void deleteCategory(@PathVariable Long categoryId) {
        log.info("(Admin)CategoryController.deleteCategory: received request to delete category with id={}", categoryId);
        adminCategoryService.deleteCategoryById(categoryId);
    }



}
