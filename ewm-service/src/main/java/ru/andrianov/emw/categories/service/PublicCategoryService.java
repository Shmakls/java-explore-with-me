package ru.andrianov.emw.categories.service;

import ru.andrianov.emw.categories.model.Category;

import java.util.List;

public interface PublicCategoryService {


    List<Category> getAllCategoriesByPages(Integer from, Integer size);

    Category getCategoryById(Long categoryId);
}
