package ru.andrianov.emw.compilations.model;

import lombok.Data;
import ru.andrianov.emw.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean pinned;

    private String title;

    @ManyToMany
    private List<Event> events;

    public void addEvent(Event event) {
        events.add(event);
    }

    public void deleteEvent(Event event) {
        events.remove(event);
    }

}
