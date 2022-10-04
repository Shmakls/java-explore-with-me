package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.SetterParamsToEventService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.mapper.CompilationMapper;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.service.AdminCompilationService;
import ru.andrianov.emw.compilations.service.CompilationService;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.exceptions.DuplicateEventException;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final EventService eventService;

    private final CompilationService compilationService;

    private final SetterParamsToEventService setterParamsToEventService;

    @Override
    public CompilationDto addNewCompilationByAdmin(CompilationToCreateDto compilationToCreateDto) {

        List<Long> eventsId = compilationToCreateDto.getEvents();

        for (Long eventId : eventsId) {
            if (!eventService.existById(eventId)) {
                log.error("AdminService.addNewCompilationByAdmin: event with id={} not exist", eventId);
                throw new EventNotFoundException("event not found");
            }
        }

        Compilation compilation = CompilationMapper.fromCreateDto(compilationToCreateDto);

        compilation.setEvents(eventsId
                .stream()
                .map(eventService::getEventById)
                .collect(Collectors.toList()));

        compilation = compilationService.addNewCompilationByAdmin(compilation);

        List<EventToCompilationDto> events = compilation.getEvents()
                .stream()
                .map(EventMapper::toCompilationDto)
                .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());

        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        compilationDto.setEvents(events);

        return compilationDto;

    }

    @Override
    public void deleteCompilationByAdminById(Long compId) {

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.deleteCompilationById: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        compilationService.deleteCompilationById(compId);

    }

    @Override
    public void deleteEventFromCompilationByIdByAdmin(Long compId, Long eventId) {

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.deleteCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        if (!eventService.existById(eventId)) {
            log.error("AdminService.deleteEventFromCompilationByIdAdmin: event with id={} not found", eventId);
            throw new EventNotFoundException("event not found");
        }

        Event event = eventService.getEventById(eventId);

        Compilation compilation = compilationService.getCompilationById(compId);
        compilation.deleteEvent(event);
        compilationService.updateCompilation(compilation);

    }

    @Override
    public void addEventToCompilationByIdByAdmin(Long compId, Long eventId) {

        if (!eventService.existById(eventId)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: event with id={} not found", eventId);
            throw new EventNotFoundException("event not found");
        }

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        Event event = eventService.getEventById(eventId);

        if (compilationService.getCompilationById(compId).getEvents().contains(event)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: event with id={} already exist in compilation with id={}",
                    eventId, compId);
            throw new DuplicateEventException("event already exist in this compilation");
        }

        Compilation compilation = compilationService.getCompilationById(compId);
        compilation.addEvent(event);
        compilationService.updateCompilation(compilation);

    }

    @Override
    public void pinOrUnpinCompilationByIdByAdmin(Long compId, boolean pinned) {

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.pinCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        Compilation compilation = compilationService.getCompilationById(compId);

        compilation.setPinned(pinned);

        compilation = compilationService.updateCompilation(compilation);

    }
}
