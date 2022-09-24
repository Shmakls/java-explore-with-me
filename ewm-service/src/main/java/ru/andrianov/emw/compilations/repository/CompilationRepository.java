package ru.andrianov.emw.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
