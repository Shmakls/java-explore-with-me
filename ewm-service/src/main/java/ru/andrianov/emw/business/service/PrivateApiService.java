package ru.andrianov.emw.business.service;

import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.requests.model.Request;

import java.util.List;

public interface PrivateApiService {
    List<EventToGetDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventToGetDto updateEventByUser(Long userId, EventToUpdateByAdminDto eventToUpdateByAdminDto);

    EventToGetDto addNewEvent(Long userId, EventToCreateDto eventToCreateDto);

    EventToGetDto getEventByIdByOwner(Long userId, Long eventId);

    EventToGetDto cancelEventByOwner(Long userId, Long eventId);

    List<Request> getRequestsByEventIdByOwner(Long userId, Long eventId);

    Request confirmRequestByEventIdByOwner(Long userId, Long eventId, Long reqId);

    Request rejectRequestByEventIdByOwner(Long userId, Long eventId, Long reqId);

    List<Request> getRequestsByUserId(Long userId);

    Request addNewRequestToEventByUser(Long userId, Long eventId);

    Request cancelRequestByRequestOwner(Long userId, Long requestId);
}
