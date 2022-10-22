package ru.andrianov.emw.events.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.categories.mapper.CategoryMapper;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.comment.mapper.CommentMapper;
import ru.andrianov.emw.comment.model.CommentState;
import ru.andrianov.emw.events.dto.*;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.Location;
import ru.andrianov.emw.users.mapper.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEventFromEventToCreateDto(EventToCreateDto eventToCreateDto) {

        Event event = new Event();

        event.setAnnotation(eventToCreateDto.getAnnotation());
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
        eventToCompilationDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        eventToCompilationDto.setConfirmedRequests(event.getConfirmedRequests());
        eventToCompilationDto.setEventDate(event.getEventDate());
        eventToCompilationDto.setInitiator(UserMapper.toUserInitiatorDto(event.getInitiator()));
        eventToCompilationDto.setPaid(event.isPaid());
        eventToCompilationDto.setTitle(event.getTitle());

        return eventToCompilationDto;

    }

    public static EventToGetDto toGetDto(Event event) {

        EventToGetDto eventToGetDto = new EventToGetDto();

        eventToGetDto.setId(event.getId());
        eventToGetDto.setAnnotation(event.getAnnotation());
        eventToGetDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        Optional.ofNullable(event.getConfirmedRequests()).ifPresent(eventToGetDto::setConfirmedRequests);
        eventToGetDto.setCreatedOn(event.getCreatedOn());
        eventToGetDto.setDescription(event.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventToGetDto.setEventDate(event.getEventDate().format(formatter));

        eventToGetDto.setInitiator(UserMapper.toUserInitiatorDto(event.getInitiator()));
        eventToGetDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventToGetDto.setPaid(event.isPaid());
        eventToGetDto.setParticipantLimit(event.getParticipantLimit());
        Optional.ofNullable(event.getPublishedOn()).ifPresent(eventToGetDto::setPublishedOn);
        eventToGetDto.setRequestModeration(event.isRequestModeration());
        eventToGetDto.setTitle(event.getTitle());
        eventToGetDto.setState(event.getState().toString());

        eventToGetDto.setComments(event.getComments()
                        .stream()
                        .filter(x -> x.getCommentState() == CommentState.PUBLISHED)
                        .map(CommentMapper::toGetEventDto)
                        .collect(Collectors.toList()));

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
        eventToUpdate.setCategory(event.getCategory());
        eventToUpdate.setConfirmedRequests(event.getConfirmedRequests());
        eventToUpdate.setPublishedOn(event.getPublishedOn());

        return eventToUpdate;

    }

}
