package ru.andrianov.emw.events.service;

import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;

import java.util.List;

public interface PrivateEventService {
    List<EventToGetDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventToGetDto updateEventByUser(Long userId, EventToUpdateByAdminDto eventToUpdateByAdminDto);

    EventToGetDto addNewEvent(Long userId, EventToCreateDto eventToCreateDto);

    EventToGetDto getEventByIdByOwner(Long userId, Long eventId);

    EventToGetDto cancelEventByOwner(Long userId, Long eventId);
}
