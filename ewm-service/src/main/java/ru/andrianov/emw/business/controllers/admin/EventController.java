package ru.andrianov.emw.business.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.business.service.AdminService;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final AdminService adminService;

    @GetMapping
    public List<EventToGetDto> searchEvents(@RequestParam List<Long> users,
                                      @RequestParam List<String> states,
                                      @RequestParam List<Long> categories,
                                      @RequestParam String rangeStart,
                                      @RequestParam String rangeEnd,
                                      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Admin)EventController.searchEvents: received request to search events by admin");

        return adminService.eventSearchByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PutMapping("/{eventId}")
    public EventToGetDto updateEventByAdmin(@RequestBody EventToCreateDto eventToCreateDto,
                                            @PathVariable Long eventId) {

        log.info("(Admin)EventController.updateEventByAdmin: received request to update event by admin");

        return adminService.updateEventByAdmin(eventToCreateDto, eventId);

    }

    @PatchMapping("/{eventId}/publish")
    public EventToGetDto publishEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.publishEventByAdmin: received request to publish event by admin");

        return adminService.publishEventByAdmin(eventId);

    }

    @PatchMapping("/{eventId}/reject")
    public EventToGetDto rejectEventByAdmin(@PathVariable Long eventId) {

        log.info("(Admin)EventController.rejectEventByAdmin: received request to reject event by admin");

        return adminService.rejectEventByAdmin(eventId);

    }



}
