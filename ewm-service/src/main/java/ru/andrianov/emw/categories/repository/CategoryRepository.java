package ru.andrianov.emw.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
