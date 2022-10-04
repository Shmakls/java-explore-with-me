package ru.andrianov.emw.business.service.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.Checker;
import ru.andrianov.emw.business.helper.SetterParamsToEventService;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.exceptions.WrongEventDateException;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.mapper.EventMapper;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.events.service.PrivateEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventService eventService;

    private final SetterParamsToEventService setterParamsToEventService;

    private final Checker checker;

    @Override
    public List<EventToGetDto> getEventsByUser(Long userId, Integer from, Integer size) {

        checker.userExistChecker(userId);

        return eventService.getEventsByUserId(userId, from, size).stream()
                .map(EventMapper::toGetDto)
                .peek(setterParamsToEventService::setCategoryNameAndInitiatorName)
                .collect(Collectors.toList());
    }

    @Override
    public EventToGetDto updateEventByUser(Long userId, EventToUpdateByAdminDto eventToUpdateByAdminDto) {

        checker.userExistChecker(userId);

        checker.eventExistChecker(eventToUpdateByAdminDto.getEventId());

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

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(eventToUpdate));

    }

    @Override
    public EventToGetDto addNewEvent(Long userId, EventToCreateDto eventToCreateDto) {

        if (eventToCreateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("PrivateApiService.addNewEvent: event date can't be is before than 2 hours from current " +
                    "time, you date={}, event date={}", eventToCreateDto.getEventDate(), LocalDateTime.now());
            throw new WrongEventDateException("wrong event date");
        }

        checker.userExistChecker(userId);

        Event event = EventMapper.toEventFromEventToCreateDto(eventToCreateDto);

        event.setInitiator(userId);

        event = eventService.addNewEvent(event);

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));

    }

    @Override
    public EventToGetDto getEventByIdByOwner(Long userId, Long eventId) {

        checker.userExistChecker(userId);

        checker.eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        checker.ownerEventChecker(userId, event.getInitiator());

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(event));
    }

    @Override
    public EventToGetDto cancelEventByOwner(Long userId, Long eventId) {

        checker.userExistChecker(userId);

        checker.eventExistChecker(eventId);

        Event event = eventService.getEventById(eventId);

        checker.ownerEventChecker(userId, event.getInitiator());

        if (event.getState() != EventState.PENDING) {
            log.error("PrivateApiService.cancelEventByOwner: event state must be WAITING to cancel, your state={}",
                    event.getState().toString());
            throw new WrongEventStateException("wrong event state");
        }

        event.setState(EventState.CANCELED);

        return setterParamsToEventService.setCategoryNameAndInitiatorName(EventMapper.toGetDto(eventService.updateEvent(event)));

    }
}
