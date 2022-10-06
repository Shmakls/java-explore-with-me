package ru.andrianov.emw.business.service.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.Checker;
import ru.andrianov.emw.events.exceptions.NoAccessRightException;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.requests.exceptions.DuplicateRequestException;
import ru.andrianov.emw.requests.exceptions.FullParticipantsException;
import ru.andrianov.emw.requests.exceptions.WrongRequestStatusException;
import ru.andrianov.emw.requests.model.Request;
import ru.andrianov.emw.requests.model.RequestStatus;
import ru.andrianov.emw.requests.service.PrivateRequestService;
import ru.andrianov.emw.requests.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final EventService eventService;

    private final RequestService requestService;

    private final Checker checker;


    @Override
    public List<Request> getRequestsByEventIdByOwner(Long userId, Long eventId) {

        checker.userExistChecker(userId);
        checker.eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        checker.ownerEventChecker(userId, event.getInitiator());

        return requestService.getRequestsByEventId(eventId);
    }

    @Override
    public Request confirmRequestByEventIdByOwner(Long userId, Long eventId, Long reqId) {

        checker.userExistChecker(userId);
        checker.eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        checker.ownerEventChecker(userId, event.getInitiator());

        checker.requestChecker(reqId);

        Request request = requestService.getRequestById(reqId);

        if (request.getStatus() != RequestStatus.PENDING) {
            log.error("PrivateApiService.confirmRequestByEventIdByOwner: request status to confirm must be PENDING," +
                    " not {}", request.getStatus().toString());
            throw new WrongRequestStatusException("wrong request status");
        }

        request.setStatus(RequestStatus.CONFIRMED);

        request = requestService.updateRequest(request);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);

        event = eventService.updateEvent(event);

        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            List<Request> requestsToReject = requestService.getRequestsWithPendingStatus(eventId)
                    .stream()
                    .peek(x -> x.setStatus(RequestStatus.REJECTED))
                    .collect(Collectors.toList());

            for (Request requestToReject : requestsToReject) {
                requestService.updateRequest(requestToReject);
            }
        }

        return request;

    }

    @Override
    public Request rejectRequestByEventIdByOwner(Long userId, Long eventId, Long reqId) {

        checker.userExistChecker(userId);
        checker.eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        checker.ownerEventChecker(userId, event.getInitiator());

        checker.requestChecker(reqId);

        Request request = requestService.getRequestById(reqId);

        if (request.getStatus() != RequestStatus.PENDING) {
            log.error("PrivateApiService.rejectRequestByEventIdByOwner: request status to reject must be PENDING," +
                    " not {}", request.getStatus().toString());
            throw new WrongRequestStatusException("wrong request status");
        }

        request.setStatus(RequestStatus.REJECTED);

        return requestService.updateRequest(request);
    }

    @Override
    public List<Request> getRequestsByUserId(Long userId) {

        checker.userExistChecker(userId);

        return requestService.getRequestsByUserId(userId);
    }

    @Override
    public Request addNewRequestToEventByUser(Long userId, Long eventId) {

        checker.userExistChecker(userId);
        checker.eventExistChecker(eventId);

        Optional<Request> checkingRequest = requestService.getRequestByUserIdAndEventId(userId, eventId);

        if (checkingRequest.isPresent()) {
            log.error("PrivateApiService.addNewRequestToEventByUser: you can't do duplicate request, your " +
                    "latest request id={}", checkingRequest.get().getId());
            throw new DuplicateRequestException("duplicate request");
        }

        Event event = eventService.getEventById(eventId);

        if (event.getInitiator().equals(userId)) {
            log.error("PrivateApiService.addNewRequestToEventByUser: you can't create request to your event, bitch");
            throw new DuplicateRequestException("request to your event");
        }

        if (event.getState() != EventState.PUBLISHED) {
            log.error("PrivateApiService.addNewRequestToEventByUser: event is not published, event status now is " +
                    "{}", event.getState().toString());
            throw new WrongEventStateException("event state not published");
        }

        if (event.getConfirmedRequests().equals(event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
            log.error("PrivateApiService.addNewRequestToEventByUser: event's participants are full");
            throw new FullParticipantsException();
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventId);
        request.setRequester(userId);
        request.setStatus(RequestStatus.PENDING);

        if (!event.isRequestModeration() || event.getParticipantLimit().equals(Long.getLong("0"))) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            event = eventService.updateEvent(event);

            return requestService.addNewRequest(request);
        }

        return requestService.addNewRequest(request);
    }

    @Override
    public Request cancelRequestByRequestOwner(Long userId, Long requestId) {

        checker.userExistChecker(userId);
        checker.requestChecker(requestId);

        Request request = requestService.getRequestById(requestId);

        if (!request.getRequester().equals(userId)) {
            log.error("PrivateApiService.cancelRequestByRequestOwner: user with id={} is not owner request with" +
                    " id={}", userId, requestId);
            throw new NoAccessRightException("no access right for request");
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestService.updateRequest(request);
    }
}
