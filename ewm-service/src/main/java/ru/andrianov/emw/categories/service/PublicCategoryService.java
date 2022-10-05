package ru.andrianov.emw.categories.service;

import ru.andrianov.emw.categories.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {


    List<CategoryDto> getAllCategoriesByPages(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);
}
