package ru.andrianov.emw.events.model;

import lombok.Data;
import ru.andrianov.emw.compilations.model.Compilation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initiator_id")
    private Long initiator;

    @Column(name = "annotation", length = 500, nullable = false)
    private String annotation;

    @Column(name = "category_id")
    private Long category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L;

    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    private int lat;

    private int lon;

    private boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit = 0L;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;

}
