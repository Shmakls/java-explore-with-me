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
import ru.andrianov.emw.compilations.model.CompilationForList;
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

    private final String SERVICE_APP = "emw-service";


    @Override
    public List<Category> getAllCategoriesByPages(Integer from, Integer size) {
        return categoryService.getCategoriesByPages(from, size);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @Override
    public List<List<EventToCompilationDto>> getCompilationsByPages(boolean pinned, Integer from, Integer size) {

        return compilationService.getAllCompilationsByPinnedByPages(pinned, from, size)
                .stream()
                .map(x -> List.of(x.getId()))
                .map(x -> compilationService.getListOfCompilationsForListByCompilationId(x.get(0)))
                .map(x -> x.stream().map(CompilationForList::getEventId).collect(Collectors.toList()))
                .map(x -> x.stream().map(eventService::getEventById).collect(Collectors.toList()))
                .map(x -> x.stream().map(EventMapper::toCompilationDto).collect(Collectors.toList()))
                .peek(x -> x.stream().peek(this::setCategoryNameAndInitiatorName).collect(Collectors.toList()))
                .collect(Collectors.toList());


    }

    @Override
    public List<EventToCompilationDto> getCompilationById(Long compilationId) {

        return compilationService.getListOfCompilationsForListByCompilationId(compilationId)
                .stream()
                .map(CompilationForList::getEventId)
                .map(eventService::getEventById)
                .map(EventMapper::toCompilationDto)
                .peek(this::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());
    }

    @Override
    public EventToGetDto getEventForPublicById(Long eventId, HttpServletRequest request) {

        Event event = eventService.getEventById(eventId);

        if (!(event.getState() == EventState.PUBLISHED)) {
            log.error("PublicApiService.getEventById: event with id={} not published", eventId);
            throw new EventNotFoundException("event not published");
        }

        eventClient.saveStat(SERVICE_APP, request.getRequestURI(), request.getRemoteAddr());

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));
    }

    @Override
    public List<EventToGetDto> searchEventsByParams(String text, List<Long> categoriesId,
                                                    boolean paid, String rangeStart, String rangeEnd,
                                                    boolean onlyAvailable, EventSort eventSort,
                                                    Integer from, Integer size, HttpServletRequest request) {

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(3);

        if(!StringUtils.hasText(rangeStart) || !StringUtils.hasText(rangeEnd)) {
            start = LocalDateTime.parse(rangeStart);
            end = LocalDateTime.parse(rangeEnd);
        }

        List<Event> events = eventService.searchEventsByText(text, categoriesId, paid,
                start, end, eventSort, from, size);

        Predicate<Event> onlyAvailablePredicate = new Predicate<Event>() {
            @Override
            public boolean test(Event event) {
                return event.getConfirmedRequests() < event.getParticipantLimit();
            }
        };

        eventClient.saveStat(SERVICE_APP, request.getRequestURI(), request.getRemoteAddr());

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

        String apiPrefix = "http://localhost:8080/events/";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(20).format(formatter);
        String end = LocalDateTime.now().format(formatter);

        List<String> uris = List.of(apiPrefix + eventId);

        Map<String, Object> params = Map.of(
            "start", start,
                "end", end,
                "uris", uris
        );

        ResponseEntity<Object> response = eventClient.getStat("stats", params);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(response.getBody().toString());
            List<EndpointStat> myObjects = Arrays.asList(mapper.readValue(node.toString(), EndpointStat[].class));
            return myObjects.get(0).getHits();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
