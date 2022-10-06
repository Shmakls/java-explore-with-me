package ru.andrianov.emw.compilations.service;

import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;

public interface AdminCompilationService {
    CompilationDto addNewCompilationByAdmin(CompilationToCreateDto compilationToCreateDto);

    void deleteCompilationByAdminById(Long compId);

    void deleteEventFromCompilationByIdByAdmin(Long compId, Long eventId);

    void addEventToCompilationByIdByAdmin(Long compId, Long eventId);

    void pinOrUnpinCompilationByIdByAdmin(Long compId, boolean b);
}
