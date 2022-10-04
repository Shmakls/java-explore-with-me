package ru.andrianov.emw.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.categories.exceptions.CategoryNotFoundException;
import ru.andrianov.emw.categories.mapper.CategoryMapper;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.repository.CategoryRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addNewCategory(CategoryDto categoryDto) {

        Category category = CategoryMapper.fromDto(categoryDto);

        log.info("CategoryService.addNewCategory: send a request to DB to add new category {}", category.getName());

        return CategoryMapper.toDto(categoryRepository.save(category));

    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        if (!existById(categoryDto.getId())) {
            log.error("CategoryService.updateCategory: category with id={} not exist", categoryDto.getId());
            throw new CategoryNotFoundException("category not exist");
        }

        Category category = CategoryMapper.fromDto(categoryDto);

        log.info("CategoryService.updateCategory: send a request to DB to update category {}", category.getId());

        return CategoryMapper.toDto(categoryRepository.save(category));

    }

    @Override
    public void deleteCategoryById(Long categoryId) {

        log.info("CategoryService.deleteCategoryById: send a request to DB to delete category with id={}", categoryId);

        categoryRepository.deleteById(categoryId);

    }

    @Override
    public boolean existById(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public String getCategoryNameById(Long categoryId) {

        if (!existById(categoryId)) {
            log.error("CategoryService.getCategoryNameById: category with id={} not exist", categoryId);
            throw new CategoryNotFoundException("category not exist");
        }

        return categoryRepository.getReferenceById(categoryId).getName();

    }

    @Override
    public Category getCategoryById(Long categoryId) {

        if (!existById(categoryId)) {
            log.error("CategoryService.getCategoryById: category with id={} not exist", categoryId);
            throw new CategoryNotFoundException("category not exist");
        }

        log.info("CategoryService.getCategoryById: send a request to DB to get category with id={}", categoryId);

        return categoryRepository.getCategoryById(categoryId);
    }

    @Override
    public List<Category> getCategoriesByPages(Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return categoryRepository.getCategoriesBy(pageable).getContent();
    }
}
