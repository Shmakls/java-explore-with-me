package ru.andrianov.emw.business.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.exceptions.NoAccessRightException;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.requests.exceptions.RequestNotFoundException;
import ru.andrianov.emw.requests.service.RequestService;
import ru.andrianov.emw.users.exceptions.UserNotFoundException;
import ru.andrianov.emw.users.service.UserService;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class Checker {

    private final UserService userService;

    private final EventService eventService;

    private final RequestService requestService;

    public void userExistChecker(Long userId) {
        if (!userService.existById(userId)) {
            log.error("Checker: user with id={} not exist", userId);
            throw new UserNotFoundException("user now found");
        }
    }

    public void eventExistChecker(Long eventID) {
        if (!eventService.existById(eventID)) {
            log.error("Checker: event with id={} not exist", eventID);
            throw new EventNotFoundException("event not found");
        }
    }

    public void ownerEventChecker(Long userId, Long initiatorId) {
        if (!initiatorId.equals(userId)) {
            log.error("Checker: NoAccessRight");
            throw new NoAccessRightException("current user is not event owner");
        }
    }

    public void requestChecker(Long requestId) {
        if (!requestService.existById(requestId)) {
            log.error("Checker: request with id={} not exist", requestId);
            throw new RequestNotFoundException("request not found");
        }
    }

}
