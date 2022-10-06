package ru.andrianov.emw.events.service;

import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;

import java.util.List;

public interface AdminEventService {
    List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventToGetDto updateEventByAdmin(EventToUpdateByAdminDto eventToUpdateByAdminDto, Long eventId);

    EventToGetDto publishEventByAdmin(Long eventId);

    EventToGetDto rejectEventByAdmin(Long eventId);
}
