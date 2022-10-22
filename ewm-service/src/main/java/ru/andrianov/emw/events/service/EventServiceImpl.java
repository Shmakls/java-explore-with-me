package ru.andrianov.emw.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.events.exceptions.EventNotFoundException;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventSort;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.repository.EventRepository;
import ru.andrianov.emw.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event addNewEvent(Event event) {

        log.info("EventService.addNewEvent: send a request to DB to create New event with title={}", event.getTitle());

        event.setState(EventState.PENDING);

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {

        log.info("EventService.updateEvent: send a request to DB to create New event with title={}", event.getTitle());

        return eventRepository.save(event);

    }

    @Override
    public Event getEventById(Long id) {

        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("event not found"));
    }

    @Override
    public void deleteEventById(Long id) {

        if (!existById(id)) {
            log.error("EventService.deleteEventById: event with id={} do not exists", id);
            throw new EventNotFoundException("event not found");
        }

        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> searchEventsByText(String text, List<Category> categories,
                                          boolean paid, LocalDateTime start, LocalDateTime end,
                                          EventSort eventSort, Integer from, Integer size) {

        String fieldToSort = "";

        if (eventSort == EventSort.EVENT_DATE) {
            fieldToSort = "eventDate";
        } else if (eventSort == EventSort.VIEWS) {
            fieldToSort = "views";
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(fieldToSort).descending());

        return eventRepository.searchEventsByTextAndParams(text, categories, paid,
                start, end, pageable)
                .getContent();
    }

    @Override
    public boolean existById(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    public List<Event> getEventsByParams(List<User> users, List<EventState> states,
                                         List<Category> categories, LocalDateTime start,
                                         LocalDateTime end, Pageable pageable) {
        return eventRepository.getEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
                users, states, categories, start, end, pageable).getContent();
    }

    @Override
    public List<Event> getEventsByUserId(User user, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());

        return eventRepository.getEventsByInitiator(user, pageable).getContent();
    }

    @Override
    public Optional<Event> getEventByCategoryId(Long categoryId) {
        return eventRepository.getFirstByCategory(categoryId);
    }

    @Override
    public String getEventTitleById(Long eventId) {
        return eventRepository.getEventTitleById(eventId);
    }
}
