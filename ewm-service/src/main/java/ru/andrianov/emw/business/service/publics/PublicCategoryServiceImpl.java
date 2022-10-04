package ru.andrianov.emw.business.service.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.categories.service.PublicCategoryService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryService categoryService;

    @Override
    public List<Category> getAllCategoriesByPages(Integer from, Integer size) {
        return categoryService.getCategoriesByPages(from, size);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
