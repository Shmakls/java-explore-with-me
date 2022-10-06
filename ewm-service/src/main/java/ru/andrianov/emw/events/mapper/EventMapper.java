package ru.andrianov.emw.events.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.Location;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEventFromEventToCreateDto(EventToCreateDto eventToCreateDto) {

        Event event = new Event();

        event.setAnnotation(eventToCreateDto.getAnnotation());
        event.setCategory(eventToCreateDto.getCategory());
        event.setDescription(eventToCreateDto.getDescription());
        event.setCreatedOn(eventToCreateDto.getCreatedOn());
        event.setEventDate(eventToCreateDto.getEventDate());
        event.setLat(eventToCreateDto.getLocation().getLat());
        event.setLon(eventToCreateDto.getLocation().getLon());
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
        eventToCompilationDto.setCategory(new CategoryDto(event.getCategory(), null));
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
        eventToGetDto.setCategory(new CategoryDto(event.getCategory(), null));
        Optional.ofNullable(event.getConfirmedRequests()).ifPresent(eventToGetDto::setConfirmedRequests);
        eventToGetDto.setCreatedOn(event.getCreatedOn());
        eventToGetDto.setDescription(event.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventToGetDto.setEventDate(event.getEventDate().format(formatter));

        eventToGetDto.setInitiator(new UserInitiatorDto(event.getInitiator()));
        eventToGetDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventToGetDto.setPaid(event.isPaid());
        eventToGetDto.setParticipantLimit(event.getParticipantLimit());
        Optional.ofNullable(event.getPublishedOn()).ifPresent(eventToGetDto::setPublishedOn);
        eventToGetDto.setRequestModeration(event.isRequestModeration());
        eventToGetDto.setTitle(event.getTitle());
        eventToGetDto.setState(event.getState().toString());

        return eventToGetDto;

    }

    public static Event eventConstructorToUpdateEvent(Event event, EventToUpdateByAdminDto eventToUpdateByAdminDto) {

        Event eventToUpdate = new Event();

        eventToUpdate.setId(eventToUpdateByAdminDto.getEventId());

        if (!(eventToUpdateByAdminDto.getAnnotation() == null)) {
            eventToUpdate.setAnnotation(eventToUpdateByAdminDto.getAnnotation());
        } else {
            eventToUpdate.setAnnotation(event.getAnnotation());
        }

        if (!(eventToUpdateByAdminDto.getCategory() == null)) {
            eventToUpdate.setCategory(eventToUpdateByAdminDto.getCategory());
        } else {
            eventToUpdate.setCategory(event.getCategory());
        }

        if (!(eventToUpdateByAdminDto.getDescription() == null)) {
            eventToUpdate.setDescription(eventToUpdateByAdminDto.getDescription());
        } else {
            eventToUpdate.setDescription(event.getDescription());
        }

        if (!(eventToUpdateByAdminDto.getCreatedOn() == null)) {
            eventToUpdate.setCreatedOn(eventToUpdateByAdminDto.getCreatedOn());
        } else {
            eventToUpdate.setCreatedOn(event.getCreatedOn());
        }

        if (!(eventToUpdateByAdminDto.getEventDate() == null)) {
            eventToUpdate.setEventDate(eventToUpdateByAdminDto.getEventDate());
        } else {
            eventToUpdate.setEventDate(event.getEventDate());
        }

        if (!(eventToUpdateByAdminDto.getLocation() == null)) {
            eventToUpdate.setLon(eventToUpdateByAdminDto.getLocation().getLon());
            eventToUpdate.setLat(eventToUpdateByAdminDto.getLocation().getLat());
        } else {
            eventToUpdate.setLon(event.getLon());
            eventToUpdate.setLat(event.getLat());
        }

        if (!(eventToUpdateByAdminDto.getPaid() == null)) {
            eventToUpdate.setPaid(eventToUpdateByAdminDto.getPaid());
        } else {
            eventToUpdate.setPaid(event.isPaid());
        }

        if (!(eventToUpdateByAdminDto.getParticipantLimit() == null)) {
            eventToUpdate.setParticipantLimit(eventToUpdateByAdminDto.getParticipantLimit());
        } else {
            eventToUpdate.setParticipantLimit(event.getParticipantLimit());
        }

        if (!(eventToUpdateByAdminDto.getRequestModeration() == null)) {
            eventToUpdate.setRequestModeration(eventToUpdateByAdminDto.getRequestModeration());
        } else {
            eventToUpdate.setRequestModeration(event.isRequestModeration());
        }

        if (!(eventToUpdateByAdminDto.getTitle() == null)) {
            eventToUpdate.setTitle(eventToUpdateByAdminDto.getTitle());
        } else {
            eventToUpdate.setTitle(event.getTitle());
        }

        eventToUpdate.setState(event.getState());
        eventToUpdate.setInitiator(event.getInitiator());
        eventToUpdate.setConfirmedRequests(event.getConfirmedRequests());
        eventToUpdate.setPublishedOn(event.getPublishedOn());

        return eventToUpdate;

    }

}
