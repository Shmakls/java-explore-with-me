package ru.andrianov.emw.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.categories.service.PublicCategoryService;
import ru.andrianov.emw.categories.model.Category;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<Category> getAllCategories(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Public)CategoryController.getAllCategories: received a request to get all categories");

        return publicCategoryService.getAllCategoriesByPages(from, size);

    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {

        log.info("(Public)CategoryController.getCategoryById: received a request to get category with id={}", categoryId);

        return publicCategoryService.getCategoryById(categoryId);

    }

}
