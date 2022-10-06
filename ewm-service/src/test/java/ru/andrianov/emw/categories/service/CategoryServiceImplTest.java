package ru.andrianov.emw.categories.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryServiceImplTest {

    private final CategoryRepository categoryRepository;

    private static Category category1;

    @BeforeAll
    static void beforeAll() {

        category1 = new Category();
        category1.setName("category1");

    }

    @Test
    void getCategoryNameById() {

        Category category = categoryRepository.save(category1);

        String categoryName = categoryRepository.getCategoryNameById(category.getId()).get();

        assertEquals(category.getName(), categoryName);

    }
}