package ru.andrianov.emw.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.events.service.PublicEventService;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping("/{eventId}")
    public EventToGetDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {

        log.info("(Public)EventController.getEventById: received a request to get event with id={}", eventId);

        return publicEventService.getEventForPublicById(eventId, request);

    }

    @GetMapping
    public List<EventToGetDto> searchEventsByParams(@RequestParam String text,
                                                    @RequestParam List<Long> categories,
                                                    @RequestParam(required = false, defaultValue = "false") boolean paid,
                                                    @RequestParam(required = false, defaultValue = "") String rangeStart,
                                                    @RequestParam(required = false, defaultValue = "") String rangeEnd,
                                                    @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
                                                    @RequestParam String sort,
                                                    @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {

        log.info("(Public)EventController.searchEventsByParams: received a request to search events with params: \n" +
                        "text={},\n" +
                        "categoriesId={},\n" +
                        "paid={},\n" +
                        "rangeStart={},\n" +
                        "rangeEnd={},\n" +
                        "onlyAvailable={},\n" +
                        "sort={},\n" +
                        "from={},\n" +
                        "size={},\n",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        EventSort eventSort = EventSort.from(sort)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + sort));

        return publicEventService.searchEventsByParams(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, eventSort, from, size, request);

    }

}
