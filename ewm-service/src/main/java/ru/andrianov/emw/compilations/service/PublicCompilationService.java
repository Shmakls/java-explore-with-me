package ru.andrianov.emw.compilations.service;

import ru.andrianov.emw.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilationsByPages(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compilationId);
}
