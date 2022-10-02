package ru.andrianov.emw.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.mapper.CompilationMapper;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.service.CompilationService;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.EndpointStat;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventSort;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.users.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicApiServiceImpl implements PublicApiService {

    private final CategoryService categoryService;

    private final CompilationService compilationService;

    private final EventService eventService;

    private final UserService userService;

    private final EventClient eventClient;

    private final String serviceApp = "emw-service";


    @Override
    public List<Category> getAllCategoriesByPages(Integer from, Integer size) {
        return categoryService.getCategoriesByPages(from, size);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @Override
    public List<CompilationDto> getCompilationsByPages(boolean pinned, Integer from, Integer size) {

        List<Compilation> compilations = compilationService.getAllCompilationsByPinnedByPages(pinned, from, size);

        List<CompilationDto> compilationDtos = new ArrayList<>();

        for (Compilation compilation : compilations) {

            CompilationDto compilationDto = CompilationMapper.toDto(compilation);

            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toCompilationDto)
                    .peek(this::setCategoryNameAndInitiatorName)
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
                .peek(this::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());

        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        compilationDto.setEvents(events);

        return compilationDto;
    }

    @Override
    public EventToGetDto getEventForPublicById(Long eventId, HttpServletRequest request) {

        Event event = eventService.getEventById(eventId);

        if (!(event.getState() == EventState.PUBLISHED)) {
            log.error("PublicApiService.getEventById: event with id={} not published", eventId);
            throw new EventNotFoundException("event not published");
        }

        eventClient.saveStat(serviceApp, request.getRequestURI(), request.getRemoteAddr());

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));
    }

    @Override
    public List<EventToGetDto> searchEventsByParams(String text, List<Long> categoriesId,
                                                    boolean paid, String rangeStart, String rangeEnd,
                                                    boolean onlyAvailable, EventSort eventSort,
                                                    Integer from, Integer size, HttpServletRequest request) {

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(3);

        if (StringUtils.hasText(rangeStart) || StringUtils.hasText(rangeEnd)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd,formatter);
        }

        List<Event> events = eventService.searchEventsByText(text, categoriesId, paid,
                start, end, eventSort, from, size);

        Predicate<Event> onlyAvailablePredicate = event -> event.getConfirmedRequests() < event.getParticipantLimit();

        eventClient.saveStat(serviceApp, request.getRequestURI(), request.getRemoteAddr());

        if (onlyAvailable) {
            return events.stream()
                    .filter(onlyAvailablePredicate)
                    .map(EventMapper::toGetDto)
                    .peek(this::setCategoryNameAndInitiatorName)
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .map(EventMapper::toGetDto)
                    .peek(this::setCategoryNameAndInitiatorName)
                    .collect(Collectors.toList());
        }

    }

    private EventToGetDto setCategoryNameAndInitiatorName(EventToGetDto eventToGetDto) {

        eventToGetDto.getCategory().setName(categoryService.getCategoryNameById(eventToGetDto.getCategory().getId()));
        eventToGetDto.getInitiator().setName(userService.getUserNameById(eventToGetDto.getInitiator().getId()));
        eventToGetDto.setViews(getViewsFromStatServiceToEventsDto(eventToGetDto.getId()));

        return eventToGetDto;
    }

    private EventToCompilationDto setCategoryNameAndInitiatorName(EventToCompilationDto eventToCompilationDto) {

        eventToCompilationDto.getCategory().setName(categoryService.getCategoryNameById(eventToCompilationDto.getCategory().getId()));
        eventToCompilationDto.getInitiator().setName(userService.getUserNameById(eventToCompilationDto.getInitiator().getId()));
        eventToCompilationDto.setViews(getViewsFromStatServiceToEventsDto(eventToCompilationDto.getId()));

        return eventToCompilationDto;
    }

    private Long getViewsFromStatServiceToEventsDto(Long eventId) {

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
