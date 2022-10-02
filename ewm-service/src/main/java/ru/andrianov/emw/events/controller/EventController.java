package ru.andrianov.emw.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.business.service.AdminService;
import ru.andrianov.emw.business.service.PrivateApiService;
import ru.andrianov.emw.business.service.PublicApiService;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final AdminService adminService;

    private final PublicApiService publicApiService;

    private final PrivateApiService privateApiService;

    @GetMapping("/admin/events")
    public List<EventToGetDto> searchEvents(@RequestParam List<Long> users,
                                      @RequestParam List<String> states,
                                      @RequestParam List<Long> categories,
                                      @RequestParam String rangeStart,
                                      @RequestParam String rangeEnd,
                                      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Admin)EventController.searchEvents: received request to search events by admin with params:\n" +
                "users={},\n" +
                "states={},\n" +
                "categories={},\n" +
                "rangeStart={},\n" +
                "rangeEnd={},\n" +
                "from={},\n" +
                "size={},\n",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return adminService.eventSearchByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PutMapping("/admin/events/{eventId}")
    public EventToGetDto updateEventByAdmin(@RequestBody EventToUpdateByAdminDto eventToUpdateByAdminDto,
                                            @PathVariable Long eventId) {

        log.info("(Admin)EventController.updateEventByAdmin: received request to update event by admin");

        return adminService.updateEventByAdmin(eventToUpdateByAdminDto, eventId);

    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventToGetDto publishEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.publishEventByAdmin: received request to publish event by admin");

        return adminService.publishEventByAdmin(eventId);

    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventToGetDto rejectEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.rejectEventByAdmin: received request to reject event by admin");

        return adminService.rejectEventByAdmin(eventId);

    }

    @GetMapping("/events/{eventId}")
    public EventToGetDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {

        log.info("(Public)EventController.getEventById: received a request to get event with id={}", eventId);

        return publicApiService.getEventForPublicById(eventId, request);

    }

    @GetMapping("/events")
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

        return publicApiService.searchEventsByParams(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, eventSort, from, size, request);

    }

    @GetMapping("/users/{userId}/events")
    public List<EventToGetDto> getEventsByUser(@PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Private)EventController.getEventsByUser: received a request to get events by user with id={}",
                userId);

        return privateApiService.getEventsByUser(userId, from, size);

    }

    @PatchMapping("/users/{userId}/events")
    public EventToGetDto updateEventByUser(@PathVariable Long userId,
                                           @RequestBody EventToUpdateByAdminDto eventToUpdateByAdminDto) {

        log.info("(Private)EventController.updateEventsByUser: received a request to update event with id={}" +
                "by user with id={}", eventToUpdateByAdminDto.getEventId(), userId);

        return privateApiService.updateEventByUser(userId, eventToUpdateByAdminDto);

    }

    @PostMapping("/users/{userId}/events")
    public EventToGetDto addNewEvent(@PathVariable Long userId,
                                     @Valid @RequestBody EventToCreateDto eventToCreateDto) {

        log.info("(Private)EventController.addNewEvent: received a request to add new event {}",
                eventToCreateDto.getTitle());

        return privateApiService.addNewEvent(userId, eventToCreateDto);

    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventToGetDto getEventByIdByOwner(@PathVariable Long userId, @PathVariable Long eventId) {

        log.info("(Private)EventController.getEventByIdByOwner: received a request to get event with id={}" +
                " by owner with id={}", eventId, userId);

        return privateApiService.getEventByIdByOwner(userId, eventId);

    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventToGetDto cancelEventByOwner(@PathVariable Long userId, @PathVariable Long eventId) {

        log.info("(Private)EventController.cancelEventByOwner: received a request to cancel event with " +
                "id={} by owner with id ={}", eventId, userId);

        return privateApiService.cancelEventByOwner(userId, eventId);

    }



}
