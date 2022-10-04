package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.categories.exceptions.CategoryNotFoundException;
import ru.andrianov.emw.categories.exceptions.CategoryStillUseException;
import ru.andrianov.emw.categories.service.AdminCategoryService;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.events.service.EventService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryService categoryService;

    private final EventService eventService;

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

        if (!categoryService.existById(categoryId)) {
            log.error("CategoryService.deleteCategoryById: category with id={} not exist", categoryId);
            throw new CategoryNotFoundException("category not exist");
        }

        if (eventService.getEventByCategoryId(categoryId).isPresent()) {
            log.error("CategoryService.deleteCategoryById: you can't delete category because it's still use");
            throw new CategoryStillUseException();
        }

        categoryService.deleteCategoryById(categoryId);

    }
}
