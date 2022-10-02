package ru.andrianov.emw.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.mapper.CompilationMapper;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.service.CompilationService;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.exceptions.DuplicateEventException;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.EndpointStat;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.users.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private final EventClient eventClient;

    @Override
    public List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states,
                                            List<Long> categories, String rangeStart,
                                            String rangeEnd, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);

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
    public EventToGetDto updateEventByAdmin(EventToUpdateByAdminDto eventToUpdateByAdminDto, Long eventId) {

        Event event = eventService.getEventById(eventId);

        Event eventToUpdate = EventMapper.eventConstructorToUpdateEvent(event, eventToUpdateByAdminDto);

        eventToUpdate = eventService.updateEvent(eventToUpdate);

        EventToGetDto eventToGetDto = EventMapper.toGetDto(eventToUpdate);

        return setCategoryNameAndInitiatorName(eventToGetDto);

    }

    @Override
    public EventToGetDto publishEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PENDING) {
            log.error("AdminService.publishEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("AdminService.publishEventByAdmin: \n" +
                    "the start of the event must be no earlier than after the date of publication");
            throw new WrongEventStateException("it's too late");
        }

        event.setState(EventState.PUBLISHED);

        event = eventService.updateEvent(event);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public EventToGetDto rejectEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PENDING) {
            log.error("AdminService.rejectEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        event.setState(EventState.CANCELED);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

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
                .peek(this::setCategoryNameAndInitiatorName)
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
    public void deleteEventFromCompilationByIdBYAdmin(Long compId, Long eventId) {

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

    private EventToGetDto setCategoryNameAndInitiatorName(EventToGetDto eventToGetDto) {

        eventToGetDto.getCategory().setName(categoryService.getCategoryNameById(eventToGetDto.getCategory().getId()));
        eventToGetDto.getInitiator().setName(userService.getUserNameById(eventToGetDto.getInitiator().getId()));

        return eventToGetDto;
    }

    private EventToCompilationDto setCategoryNameAndInitiatorName(EventToCompilationDto eventToCompilationDto) {

        eventToCompilationDto.getCategory().setName(categoryService.getCategoryNameById(eventToCompilationDto.getCategory().getId()));
        eventToCompilationDto.getInitiator().setName(userService.getUserNameById(eventToCompilationDto.getInitiator().getId()));
        eventToCompilationDto.setViews(getViewsFromStatServiceToEventsDto(eventToCompilationDto.getId()));

        return eventToCompilationDto;
    }

    public Long getViewsFromStatServiceToEventsDto(Long eventId) {

        String apiPrefix = "/events/";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(20).format(formatter);
        String end = LocalDateTime.now().format(formatter);

        List<String> uris = List.of(apiPrefix + eventId);

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        ResponseEntity<Object> response = eventClient.getStat("/stats", params);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(response.getBody().toString());
            List<EndpointStat> myObjects = Arrays.asList(mapper.readValue(node.toString(), EndpointStat[].class));
            if (myObjects.size() > 0) {
                return myObjects.get(0).getHits();
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
