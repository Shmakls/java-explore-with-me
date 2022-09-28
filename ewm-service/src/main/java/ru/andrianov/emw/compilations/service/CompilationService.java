package ru.andrianov.emw.compilations.service;

import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.model.CompilationForList;

import java.util.List;

public interface CompilationService {
    Compilation addNewCompilationByAdmin(Compilation compilation);

    CompilationForList saveCompilationList(CompilationForList compilationForList);

    boolean existCompilationById(Long compilationId);

    void deleteCompilationById(Long compilationId);

    boolean existEventInCompilationById(Long compilationId, Long eventId);

    void deleteEventFromCompilationListById(Long eventId);

    Compilation getCompilationById(Long compilationId);

    Compilation updateCompilationById(Compilation compilation);

    List<CompilationForList> getListOfCompilationsForListByCompilationId(Long compilationId);

    List<Compilation> getAllCompilationsByPinnedByPages(boolean pinned, Integer from, Integer size);

}
