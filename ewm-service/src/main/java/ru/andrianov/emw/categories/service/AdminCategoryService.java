package ru.andrianov.emw.categories.service;

import ru.andrianov.emw.categories.dto.CategoryDto;

public interface AdminCategoryService {
    CategoryDto addNewCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategoryById(Long categoryId);
}
