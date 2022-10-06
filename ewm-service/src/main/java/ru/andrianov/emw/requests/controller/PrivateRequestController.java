package ru.andrianov.emw.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.requests.service.PrivateRequestService;
import ru.andrianov.emw.requests.model.Request;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @GetMapping("/events/{eventId}/requests")
    public List<Request> getRequestsByEventIdByOwner(@PathVariable Long userId, @PathVariable Long eventId) {

        log.info("(Private)RequestController.getRequestsByEventIdByOwner: received a request to get " +
                "requests to event with id={} by owner with id={}", eventId, userId);

        return privateRequestService.getRequestsByEventIdByOwner(userId, eventId);

    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public Request confirmRequestByEventIdByOwner(@PathVariable Long userId, @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {

        log.info("(Private)RequestController.confirmRequestByEventIdByOwner: received a request to confirm " +
                "request with id={} event with id={} by owner with id={}", reqId, eventId, userId);

        return privateRequestService.confirmRequestByEventIdByOwner(userId, eventId, reqId);

    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public Request rejectRequestByEventIdByOwner(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {

        log.info("(Private)RequestController.rejectRequestByEventIdByOwner: received a request to reject " +
                "request with id={} event with id={} by owner with id={}", reqId, eventId, userId);

        return privateRequestService.rejectRequestByEventIdByOwner(userId, eventId, reqId);

    }

    @GetMapping("/requests")
    public List<Request> getRequestsByUserId(@PathVariable Long userId) {

        log.info("(Private)RequestController.getRequestsByUserId: received a request to get requests by user with " +
                "id={}", userId);

        return privateRequestService.getRequestsByUserId(userId);

    }

    @PostMapping("/requests")
    public Request addNewRequestToEventByUser(@PathVariable Long userId, @RequestParam Long eventId) {

        log.info("(Private)RequestController.addNewRequestToEventByUser: received a request to add new request " +
                "to event with id={} by user with id={}", eventId, userId);

        return privateRequestService.addNewRequestToEventByUser(userId, eventId);

    }

    @PatchMapping("/requests/{requestId}/cancel")
    public Request cancelRequestByRequestOwner(@PathVariable Long userId, @PathVariable Long requestId) {

        log.info("(Private)RequestController.cancelRequestByRequestOwner: received a request to cancel request " +
                "with id={} by owner with id={}", requestId, userId);

        return privateRequestService.cancelRequestByRequestOwner(userId, requestId);

    }

}
