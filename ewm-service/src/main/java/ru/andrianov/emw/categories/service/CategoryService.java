package ru.andrianov.emw.categories.service;

import ru.andrianov.emw.categories.model.Category;

import java.util.List;

public interface CategoryService {
    Category addNewCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategoryById(Long categoryId);

    boolean existById(Long categoryId);

    String getCategoryNameById(Long categoryId);

    Category getCategoryById(Long categoryId);

    List<Category> getCategoriesByPages(Integer from, Integer size);
}
