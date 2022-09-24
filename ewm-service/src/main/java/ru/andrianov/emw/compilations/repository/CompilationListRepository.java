package ru.andrianov.emw.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.compilations.model.CompilationForList;

import java.util.List;

public interface CompilationListRepository extends JpaRepository<CompilationForList, Long> {

    void deleteAllByCompilationId(Long compilationId);

    List<CompilationForList> findAllByCompilationId(Long compilationId);

    void deleteByEventId(Long eventId);

}
