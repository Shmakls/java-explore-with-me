package ru.andrianov.emw.events.mapper;

import lombok.RequiredArgsConstructor;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.Location;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

import java.util.Optional;

@RequiredArgsConstructor
public class EventMapper {

    public static Event toEventFromEventToCreateDto(EventToCreateDto eventToCreateDto) {

        Event event = new Event();

        event.setInitiator(event.getInitiator());
        event.setAnnotation(eventToCreateDto.getAnnotation());
        event.setCategory(eventToCreateDto.getCategory());
        event.setDescription(eventToCreateDto.getDescription());
        event.setCreatedOn(eventToCreateDto.getCreatedOn());
        event.setEventDate(eventToCreateDto.getEventDate());
        event.setLocation(eventToCreateDto.getLocation());
        event.setPaid(eventToCreateDto.isPaid());
        event.setParticipantLimit(eventToCreateDto.getParticipantLimit());
        event.setRequestModeration(eventToCreateDto.isRequestModeration());
        event.setTitle(eventToCreateDto.getTitle());

        return event;

    }

    public static EventToCompilationDto toCompilationDto(Event event) {

        EventToCompilationDto eventToCompilationDto = new EventToCompilationDto();

        eventToCompilationDto.setId(event.getId());
        eventToCompilationDto.setAnnotation(event.getAnnotation());
        eventToCompilationDto.setCategory(new Category(event.getCategory()));
        eventToCompilationDto.setConfirmedRequests(event.getConfirmedRequests());
        eventToCompilationDto.setEventDate(event.getEventDate());
        eventToCompilationDto.setInitiator(new UserInitiatorDto(event.getInitiator()));
        eventToCompilationDto.setPaid(event.isPaid());
        eventToCompilationDto.setTitle(event.getTitle());

        return eventToCompilationDto;

    }

    public static EventToGetDto toGetDto(Event event) {

        EventToGetDto eventToGetDto = new EventToGetDto();

        eventToGetDto.setId(event.getId());
        eventToGetDto.setAnnotation(event.getAnnotation());
        eventToGetDto.setCategory(new Category(event.getCategory()));
        Optional.ofNullable(event.getConfirmedRequests()).ifPresent(eventToGetDto::setConfirmedRequests);
        eventToGetDto.setCreatedOn(event.getCreatedOn());
        eventToGetDto.setDescription(event.getDescription());
        eventToGetDto.setEventDate(event.getEventDate());
        eventToGetDto.setInitiator(new UserInitiatorDto(event.getInitiator()));
        eventToGetDto.setLocation(event.getLocation());
        eventToGetDto.setPaid(event.isPaid());
        eventToGetDto.setParticipantLimit(event.getParticipantLimit());
        Optional.ofNullable(event.getPublishedOn()).ifPresent(eventToGetDto::setPublishedOn);
        eventToGetDto.setRequestModeration(event.isRequestModeration());
        eventToGetDto.setTitle(event.getTitle());
        eventToGetDto.setState(event.getState().toString());

        return eventToGetDto;

    }

}
