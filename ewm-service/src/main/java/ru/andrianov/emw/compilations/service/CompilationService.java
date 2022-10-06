package ru.andrianov.emw.compilations.service;

import ru.andrianov.emw.compilations.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation addNewCompilationByAdmin(Compilation compilation);

    boolean existCompilationById(Long compilationId);

    void deleteCompilationById(Long compilationId);

    Compilation getCompilationById(Long compilationId);

    Compilation updateCompilation(Compilation compilation);

    List<Compilation> getAllCompilationsByPinnedByPages(boolean pinned, Integer from, Integer size);

}
