package ru.andrianov.emw.compilations.model;

import lombok.Data;
import ru.andrianov.emw.events.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "pinned status should be not null")
    private boolean pinned;

    @NotNull(message = "compilation title should be not null")
    @NotEmpty(message = "compilation title should be not empty")
    private String title;

    @NotNull
    @OneToMany
    private List<Event> events;

    public void addEvent(Event event) {
        events.add(event);
    }

    public void deleteEvent(Event event) {
        events.remove(event);
    }

}
