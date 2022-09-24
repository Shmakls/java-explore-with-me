package ru.andrianov.emw.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.mapper.CompilationMapper;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.model.CompilationForList;
import ru.andrianov.emw.compilations.service.CompilationService;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.exceptions.DuplicateEventException;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final EventService eventService;

    private final UserService userService;

    private final CategoryService categoryService;

    private final CompilationService compilationService;

    @Override
    public List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states,
                                            List<Long> categories, String rangeStart,
                                            String rangeEnd, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());

        LocalDateTime start = LocalDateTime.parse(rangeStart);
        LocalDateTime end = LocalDateTime.parse(rangeEnd);

        List<EventState> eventStates = states.stream()
                .map(EventState::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Event> events = eventService.getEventsByParams(users, eventStates, categories, start, end, pageable);

        return events.stream()
                .map(EventMapper::toGetDto)
                .peek(this::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());

    }

    @Override
    public EventToGetDto updateEventByAdmin(EventToCreateDto eventToCreateDto, Long eventId) {

        Event event = eventService.updateEvent(EventMapper.toEventFromEventToCreateDto(eventToCreateDto));

        EventToGetDto eventToGetDto = EventMapper.toGetDto(event);

        return setCategoryNameAndInitiatorName(eventToGetDto);

    }

    @Override
    public EventToGetDto publishEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.WAITING) {
            log.error("AdminService.publishEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("AdminService.publishEventByAdmin: \n" +
                    "the start of the event must be no earlier than after the date of publication");
            throw new WrongEventStateException("it's too late");
        }

        event.setState(EventState.PUBLISHED);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public EventToGetDto rejectEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.WAITING) {
            log.error("AdminService.rejectEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        event.setState(EventState.REJECTED);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public List<EventToCompilationDto> addNewCompilationByAdmin(CompilationDto compilationDto) {

        Compilation compilation = compilationService.addNewCompilationByAdmin(CompilationMapper.fromDto(compilationDto));

        List<Long> eventsId = compilationDto.getEvents();

        for (Long eventId : eventsId) {

            if(!eventService.existById(eventId)) {
                log.error("AdminService.addNewCompilationByAdmin: event with id={} not found", eventId);
                throw new EventNotFoundException("event not found");
            }

            CompilationForList compilationForList = new CompilationForList();
            compilationForList.setEventId(eventId);
            compilationForList.setCompilationId(compilation.getId());
            compilationService.saveCompilationList(compilationForList);

        }

        return eventsId.stream()
                .map(eventService::getEventById)
                .map(EventMapper::toCompilationDto)
                .collect(Collectors.toList());

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
    public void deleteEventFromCompilationByIdBYAdmin(Long compId, Long eventId) {

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.deleteCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        if (!compilationService.existEventInCompilationById(compId, eventId)) {
            log.error("AdminService.deleteCompilationById: event with id={} do not exist in compilation with id={}",
                    eventId, compId);
            throw new EventNotFoundException("event not found");
        }

        compilationService.deleteEventFromCompilationListById(eventId);

    }

    @Override
    public void addEventToCompilationByIdByAdmin(Long compId, Long eventId) {

        if(!eventService.existById(eventId)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: event with id={} not found", eventId);
            throw new EventNotFoundException("event not found");
        }

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        if (compilationService.existEventInCompilationById(compId, eventId)) {
            log.error("AdminService.addEventToCompilationByIdByAdmin: event with id={} already exist in compilation with id={}",
                    eventId, compId);
            throw new DuplicateEventException("event already exist in this compilation");
        }

        CompilationForList compilationForList = new CompilationForList();
        compilationForList.setCompilationId(compId);
        compilationForList.setEventId(eventId);

        compilationForList = compilationService.saveCompilationList(compilationForList);

    }

    @Override
    public void pinOrUnpinCompilationByIdByAdmin(Long compId, boolean pinned) {

        if (!compilationService.existCompilationById(compId)) {
            log.error("AdminService.pinCompilationByIdByAdmin: compilation with id={} not found", compId);
            throw new CompilationNotFoundException("compilation not found");
        }

        Compilation compilation = compilationService.getCompilationById(compId);

        compilation.setPinned(pinned);

        compilation = compilationService.updateCompilationById(compilation);

    }

    private EventToGetDto setCategoryNameAndInitiatorName(EventToGetDto eventToGetDto) {

        eventToGetDto.getCategory().setName(categoryService.getCategoryNameById(eventToGetDto.getCategory().getId()));
        eventToGetDto.getInitiator().setName(userService.getUserNameById(eventToGetDto.getInitiator().getId()));

        return eventToGetDto;
    }
}
