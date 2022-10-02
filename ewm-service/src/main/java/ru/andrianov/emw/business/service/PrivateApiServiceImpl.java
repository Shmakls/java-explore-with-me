package ru.andrianov.emw.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.service.CategoryService;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.exceptions.NoAccessRightException;
import ru.andrianov.emw.events.exceptions.WrongEventDateException;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.EndpointStat;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.requests.exceptions.DuplicateRequestException;
import ru.andrianov.emw.requests.exceptions.FullParticipantsException;
import ru.andrianov.emw.requests.exceptions.RequestNotFoundException;
import ru.andrianov.emw.requests.exceptions.WrongRequestStatusException;
import ru.andrianov.emw.requests.model.Request;
import ru.andrianov.emw.requests.model.RequestStatus;
import ru.andrianov.emw.requests.service.RequestService;
import ru.andrianov.emw.users.exceptions.UserNotFoundException;
import ru.andrianov.emw.users.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateApiServiceImpl implements PrivateApiService {

    private final UserService userService;

    private final EventService eventService;

    private final RequestService requestService;

    private final CategoryService categoryService;

    private final EventClient eventClient;

    @Override
    public List<EventToGetDto> getEventsByUser(Long userId, Integer from, Integer size) {

        userExistChecker(userId);

        return eventService.getEventsByUserId(userId, from, size).stream()
                .map(EventMapper::toGetDto)
                .peek(this::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());
    }

    @Override
    public EventToGetDto updateEventByUser(Long userId, EventToUpdateByAdminDto eventToUpdateByAdminDto) {

        userExistChecker(userId);

        eventExistChecker(eventToUpdateByAdminDto.getEventId());

        Event event = eventService.getEventById(eventToUpdateByAdminDto.getEventId());

        if (!(event.getState() == EventState.REJECTED || event.getState() == EventState.PENDING)) {
            log.error("PrivateApiService.updateEventsByUser: you can change event only with status WAITING " +
                    "or REJECTED, current status={}", event.getState().toString());
            throw new WrongEventStateException("wrong event state");
        }

        if (eventToUpdateByAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("PrivateApiService.updateEventsByUser: event date can't be is before than 2 hours from current " +
                    "time, you date={}, event date={}", eventToUpdateByAdminDto.getEventDate(), LocalDateTime.now());
            throw new WrongEventDateException("wrong event date");
        }

        if (event.getState() == EventState.REJECTED) {
            event.setState(EventState.PENDING);
        }

        eventToUpdateByAdminDto.setCreatedOn(event.getCreatedOn());
        Event eventToUpdate = EventMapper.eventConstructorToUpdateEvent(event, eventToUpdateByAdminDto);
        eventToUpdate = eventService.updateEvent(eventToUpdate);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(eventToUpdate));

    }

    @Override
    public EventToGetDto addNewEvent(Long userId, EventToCreateDto eventToCreateDto) {

        if (eventToCreateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("PrivateApiService.addNewEvent: event date can't be is before than 2 hours from current " +
                    "time, you date={}, event date={}", eventToCreateDto.getEventDate(), LocalDateTime.now());
            throw new WrongEventDateException("wrong event date");
        }

        userExistChecker(userId);

        Event event = EventMapper.toEventFromEventToCreateDto(eventToCreateDto);

        event.setInitiator(userId);

        event = eventService.addNewEvent(event);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public EventToGetDto getEventByIdByOwner(Long userId, Long eventId) {

        userExistChecker(userId);

        eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        ownerEventChecker(userId, event.getInitiator());

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));
    }

    @Override
    public EventToGetDto cancelEventByOwner(Long userId, Long eventId) {

        userExistChecker(userId);

        eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        ownerEventChecker(userId, event.getInitiator());

        if (event.getState() != EventState.PENDING) {
            log.error("PrivateApiService.cancelEventByOwner: event state must be WAITING to cancel, your state={}",
                    event.getState().toString());
            throw new WrongEventStateException("wrong event state");
        }

        event.setState(EventState.CANCELED);

        return setCategoryNameAndInitiatorName(EventMapper.toGetDto(eventService.updateEvent(event)));

    }

    @Override
    public List<Request> getRequestsByEventIdByOwner(Long userId, Long eventId) {

        userExistChecker(userId);
        eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        ownerEventChecker(userId, event.getInitiator());

        return requestService.getRequestsByEventId(eventId);
    }

    @Override
    public Request confirmRequestByEventIdByOwner(Long userId, Long eventId, Long reqId) {

        userExistChecker(userId);
        eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        ownerEventChecker(userId, event.getInitiator());

        requestChecker(reqId);

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

        userExistChecker(userId);
        eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        ownerEventChecker(userId, event.getInitiator());

        requestChecker(reqId);

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

        userExistChecker(userId);

        return requestService.getRequestsByUserId(userId);
    }

    @Override
    public Request addNewRequestToEventByUser(Long userId, Long eventId) {

        userExistChecker(userId);
        eventExistChecker(eventId);

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

        userExistChecker(userId);
        requestChecker(requestId);

        Request request = requestService.getRequestById(requestId);

        if (!request.getRequester().equals(userId)) {
            log.error("PrivateApiService.cancelRequestByRequestOwner: user with id={} is not owner request with" +
                            " id={}", userId, requestId);
            throw new NoAccessRightException("no access right for request");
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestService.updateRequest(request);
    }

    private EventToGetDto setCategoryNameAndInitiatorName(EventToGetDto eventToGetDto) {

        eventToGetDto.getCategory().setName(categoryService.getCategoryNameById(eventToGetDto.getCategory().getId()));
        eventToGetDto.getInitiator().setName(userService.getUserNameById(eventToGetDto.getInitiator().getId()));
        eventToGetDto.setViews(getViewsFromStatServiceToEventsDto(eventToGetDto.getId()));

        return eventToGetDto;
    }

    private Long getViewsFromStatServiceToEventsDto(Long eventId) {

        String apiPrefix = "/events/";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(20).format(formatter);
        String end = LocalDateTime.now().format(formatter);

        List<String> uris = List.of(apiPrefix + eventId);

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        ResponseEntity<Object> response = eventClient.getStat("/stats", params);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(response.getBody().toString());
            List<EndpointStat> myObjects = Arrays.asList(mapper.readValue(node.toString(), EndpointStat[].class));

            if (myObjects.size() > 0) {
                return myObjects.get(0).getHits();
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void userExistChecker(Long userId) {
        if (!userService.existById(userId)) {
            log.error("PrivateApiService: user with id={} not exist", userId);
            throw new UserNotFoundException("user now found");
        }
    }

    private void eventExistChecker(Long eventID) {
        if (!eventService.existById(eventID)) {
            log.error("PrivateApiService: event with id={} not exist", eventID);
            throw new EventNotFoundException("event not found");
        }
    }

    private void ownerEventChecker(Long userId, Long initiatorId) {
        if (!initiatorId.equals(userId)) {
            log.error("PrivateApiService: NoAccessRight");
            throw new NoAccessRightException("current user is not event owner");
        }
    }

    private void requestChecker(Long requestId) {
        if (!requestService.existById(requestId)) {
            log.error("PrivateApiService: request with id={} not exist", requestId);
            throw new RequestNotFoundException("request not found");
        }
    }

}
