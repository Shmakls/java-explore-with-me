package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.DateTimeStringConverter;
import ru.andrianov.emw.business.helper.SetterParamsToEventService;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.AdminEventService;
import ru.andrianov.emw.events.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventService eventService;

    private final SetterParamsToEventService setterParamsToEventService;

    @Override
    public List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states,
                                                  List<Long> categories, String rangeStart,
                                                  String rangeEnd, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());

        LocalDateTime start = DateTimeStringConverter.fromFormattedString(rangeStart);
        LocalDateTime end = DateTimeStringConverter.fromFormattedString(rangeEnd);

        List<EventState> eventStates = states.stream()
                .map(EventState::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Event> events = eventService.getEventsByParams(users, eventStates, categories, start, end, pageable);

        return events.stream()
                .map(EventMapper::toGetDto)
                .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());

    }

    @Override
    public EventToGetDto updateEventByAdmin(EventToUpdateByAdminDto eventToUpdateByAdminDto, Long eventId) {

        Event event = eventService.getEventById(eventId);

        Event eventToUpdate = EventMapper.eventConstructorToUpdateEvent(event, eventToUpdateByAdminDto);

        eventToUpdate = eventService.updateEvent(eventToUpdate);

        EventToGetDto eventToGetDto = EventMapper.toGetDto(eventToUpdate);

        return setterParamsToEventService.setCategoryNameAndInitiatorName(eventToGetDto);

    }

    @Override
    public EventToGetDto publishEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PENDING) {
            log.error("AdminService.publishEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("AdminService.publishEventByAdmin: \n" +
                    "the start of the event must be no earlier than after the date of publication");
            throw new WrongEventStateException("it's too late");
        }

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        event = eventService.updateEvent(event);

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public EventToGetDto rejectEventByAdmin(Long eventId) {

        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PENDING) {
            log.error("AdminService.rejectEventByAdmin: event state should be WAITING, not {}",event.getState().toString());
            throw new WrongEventStateException("status to change must be WAITING");
        }

        event.setState(EventState.CANCELED);

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

}
