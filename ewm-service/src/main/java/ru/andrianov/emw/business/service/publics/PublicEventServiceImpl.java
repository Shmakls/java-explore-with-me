package ru.andrianov.emw.business.service.publics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.andrianov.emw.business.helper.DateTimeStringConverter;
import ru.andrianov.emw.business.helper.SetterParamsToEventService;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventSort;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.events.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final String serviceApp;

    private final EventService eventService;

    private final CategoryService categoryService;

    private final SetterParamsToEventService setterParamsToEventService;

    private final EventClient eventClient;

    @Autowired
    public PublicEventServiceImpl(EventService eventService,
                                  CategoryService categoryService,
                                  SetterParamsToEventService setterParamsToEventService,
                                  EventClient eventClient,
                                  @Value("${service-app.name}") String serviceApp) {
        this.eventService = eventService;
        this.setterParamsToEventService = setterParamsToEventService;
        this.eventClient = eventClient;
        this.serviceApp = serviceApp;
        this.categoryService = categoryService;
    }

    @Override
    public EventToGetDto getEventForPublicById(Long eventId, HttpServletRequest request) {

        Event event = eventService.getEventById(eventId);

        if (!(event.getState() == EventState.PUBLISHED)) {
            log.error("PublicApiService.getEventById: event with id={} not published", eventId);
            throw new EventNotFoundException("event not published");
        }

        EventToGetDto eventToGetDto = setterParamsToEventService
                .setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

        eventClient.saveStat(serviceApp, request.getRequestURI(), request.getRemoteAddr());

        return eventToGetDto;
    }

    @Override
    public List<EventToGetDto> searchEventsByParams(String text, List<Long> categoriesId,
                                                    boolean paid, String rangeStart, String rangeEnd,
                                                    boolean onlyAvailable, EventSort eventSort,
                                                    Integer from, Integer size, HttpServletRequest request) {

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(3);

        if (StringUtils.hasText(rangeStart) || StringUtils.hasText(rangeEnd)) {
            start = DateTimeStringConverter.fromFormattedString(rangeStart);
            end = DateTimeStringConverter.fromFormattedString(rangeEnd);
        }

        List<Category> categories = categoriesId
                .stream()
                .map(categoryService::getCategoryById)
                .collect(Collectors.toList());

        List<Event> events = eventService.searchEventsByText(text, categories, paid,
                start, end, eventSort, from, size);

        Predicate<Event> onlyAvailablePredicate = event -> event.getConfirmedRequests() < event.getParticipantLimit();

        eventClient.saveStat(serviceApp, request.getRequestURI(), request.getRemoteAddr());

        if (onlyAvailable) {
            return events.stream()
                    .filter(onlyAvailablePredicate)
                    .map(EventMapper::toGetDto)
                    .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .map(EventMapper::toGetDto)
                    .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                    .collect(Collectors.toList());
        }

    }
}
