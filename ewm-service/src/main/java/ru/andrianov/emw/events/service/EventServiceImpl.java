package ru.andrianov.emw.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event addNewEvent(Event event) {

        log.info("EventService.addNewEvent: send a request to DB to create New event with title {}", event.getTitle());

        event.setState(EventState.WAITING);

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {

        log.info("EventService.updateEvent: send a request to DB to create New event with title {}", event.getTitle());

        return eventRepository.save(event);

    }

    @Override
    public Event getEventById(Long id) {

        if (!existById(id)) {
            log.error("EventService.getEventById: event with id={} do not exists", id);
            throw new EventNotFoundException("event not found");
        }

        return eventRepository.getReferenceById(id);
    }

    @Override
    public void deleteEventById(Long id) {

    }

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    public List<Event> getEventsByParams(List<Long> users, List<EventState> states,
                                         List<Long> categories, LocalDateTime start,
                                         LocalDateTime end, Pageable pageable) {
        return eventRepository.getEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
                users, states, categories, start, end, pageable).getContent();
    }
}
