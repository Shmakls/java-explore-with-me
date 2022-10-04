package ru.andrianov.emw.business.service.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.business.helper.SetterParamsToEventService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.mapper.CompilationMapper;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.service.CompilationService;
import ru.andrianov.emw.compilations.service.PublicCompilationService;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationService compilationService;

    private final SetterParamsToEventService setterParamsToEventService;

    @Override
    public List<CompilationDto> getCompilationsByPages(boolean pinned, Integer from, Integer size) {

        List<Compilation> compilations = compilationService.getAllCompilationsByPinnedByPages(pinned, from, size);

        List<CompilationDto> compilationDtos = new ArrayList<>();

        for (Compilation compilation : compilations) {

            CompilationDto compilationDto = CompilationMapper.toDto(compilation);

            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toCompilationDto)
                    .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                    .collect(Collectors.toList()));

            compilationDtos.add(compilationDto);

        }

        return compilationDtos;

    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {

        if (!compilationService.existCompilationById(compilationId)) {
            log.error("PublicApiService.getCompilationById: compilation with id={} not exist", compilationId);
            throw new CompilationNotFoundException("compilation not found");
        }

        Compilation compilation = compilationService.getCompilationById(compilationId);

        List<EventToCompilationDto> events = compilation.getEvents()
                .stream()
                .map(EventMapper::toCompilationDto)
                .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());

        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        compilationDto.setEvents(events);

        return compilationDto;
    }
}
