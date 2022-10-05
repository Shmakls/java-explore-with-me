package ru.andrianov.emw.business.service.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.categories.mapper.CategoryMapper;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.categories.service.PublicCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryService categoryService;

    @Override
    public List<CategoryDto> getAllCategoriesByPages(Integer from, Integer size) {
        log.info("PublicCategoryService.getAllCategoriesByPages: send request to categoryService");

        return categoryService.getCategoriesByPages(from, size)
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.toDto(categoryService.getCategoryById(categoryId));
    }
}
