package ru.andrianov.emw.events.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> getEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(List<Long> users,
                                                                                 List<EventState> states,
                                                                                 List<Long> categories,
                                                                                 LocalDateTime start,
                                                                                 LocalDateTime end,
                                                                                 Pageable pageable);

}
