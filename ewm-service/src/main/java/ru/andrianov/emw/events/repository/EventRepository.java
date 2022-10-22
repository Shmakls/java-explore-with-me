package ru.andrianov.emw.events.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.events.model.Event;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> getEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(List<User> users,
                                                                                 List<EventState> states,
                                                                                 List<Category> categories,
                                                                                 LocalDateTime start,
                                                                                 LocalDateTime end,
                                                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (e.annotation like concat('%', ?1, '%') or e.description like concat('%', ?1, '%')) " +
            "and e.category in ?2 and e.paid = ?3 and e.eventDate between ?4 and ?5")
    Page<Event> searchEventsByTextAndParams(
            String text, List<Category> categoriesId, boolean paid,
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Event> getEventsByInitiator(User user, Pageable pageable);

    Optional<Event> getFirstByCategory(Long categoryId);

    @Query("select e.title from Event e where e.id = ?1")
    String getEventTitleById(Long eventId);

}
