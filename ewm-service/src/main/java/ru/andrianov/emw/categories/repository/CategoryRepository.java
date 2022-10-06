package ru.andrianov.emw.categories.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andrianov.emw.categories.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> getCategoriesBy(Pageable pageable);


    @Query("select c.name from Category c where c.id = ?1")
    Optional<String> getCategoryNameById(Long categoryId);

}
