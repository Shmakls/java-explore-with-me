package ru.andrianov.emw.events.service;

import org.springframework.data.domain.Pageable;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Event addNewEvent(Event event);

    Event updateEvent(Event event);

    Event getEventById(Long id);

    void deleteEventById(Long id);

    List<Event> getAllEvents();

    boolean existById(Long id);

    List<Event> getEventsByParams(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime start, LocalDateTime end, Pageable pageable);
}
