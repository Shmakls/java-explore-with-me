package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.categories.service.AdminCategoryService;
import ru.andrianov.emw.categories.service.CategoryService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryService categoryService;

    @Override
    public CategoryDto addNewCategory(CategoryDto categoryDto) {
        return categoryService.addNewCategory(categoryDto);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {

        categoryService.deleteCategoryById(categoryId);

    }
}
