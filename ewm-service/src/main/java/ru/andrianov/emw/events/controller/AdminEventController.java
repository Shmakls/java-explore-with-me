package ru.andrianov.emw.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.events.service.AdminEventService;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
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

        return adminEventService.eventSearchByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PutMapping("/{eventId}")
    public EventToGetDto updateEventByAdmin(@RequestBody EventToUpdateByAdminDto eventToUpdateByAdminDto,
                                            @PathVariable Long eventId) {

        log.info("(Admin)EventController.updateEventByAdmin: received request to update event by admin");

        return adminEventService.updateEventByAdmin(eventToUpdateByAdminDto, eventId);

    }

    @PatchMapping("/{eventId}/publish")
    public EventToGetDto publishEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.publishEventByAdmin: received request to publish event by admin");

        return adminEventService.publishEventByAdmin(eventId);

    }

    @PatchMapping("/{eventId}/reject")
    public EventToGetDto rejectEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.rejectEventByAdmin: received request to reject event by admin");

        return adminEventService.rejectEventByAdmin(eventId);

    }

}
