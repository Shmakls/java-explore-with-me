package ru.andrianov.emw.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.events.service.PrivateEventService;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @GetMapping
    public List<EventToGetDto> getEventsByUser(@PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Private)EventController.getEventsByUser: received a request to get events by user with id={}",
                userId);

        return privateEventService.getEventsByUser(userId, from, size);

    }

    @PatchMapping
    public EventToGetDto updateEventByUser(@PathVariable Long userId,
                                           @RequestBody EventToUpdateByAdminDto eventToUpdateByAdminDto) {

        log.info("(Private)EventController.updateEventsByUser: received a request to update event with id={}" +
                "by user with id={}", eventToUpdateByAdminDto.getEventId(), userId);

        return privateEventService.updateEventByUser(userId, eventToUpdateByAdminDto);

    }

    @PostMapping
    public EventToGetDto addNewEvent(@PathVariable Long userId,
                                     @Valid @RequestBody EventToCreateDto eventToCreateDto) {

        log.info("(Private)EventController.addNewEvent: received a request to add new event {}",
                eventToCreateDto.getTitle());

        return privateEventService.addNewEvent(userId, eventToCreateDto);

    }

    @GetMapping("/{eventId}")
    public EventToGetDto getEventByIdByOwner(@PathVariable Long userId, @PathVariable Long eventId) {

        log.info("(Private)EventController.getEventByIdByOwner: received a request to get event with id={}" +
                " by owner with id={}", eventId, userId);

        return privateEventService.getEventByIdByOwner(userId, eventId);

    }

    @PatchMapping("/{eventId}")
    public EventToGetDto cancelEventByOwner(@PathVariable Long userId, @PathVariable Long eventId) {

        log.info("(Private)EventController.cancelEventByOwner: received a request to cancel event with " +
                "id={} by owner with id ={}", eventId, userId);

        return privateEventService.cancelEventByOwner(userId, eventId);

    }



}
