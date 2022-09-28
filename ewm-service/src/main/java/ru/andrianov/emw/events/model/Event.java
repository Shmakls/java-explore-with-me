package ru.andrianov.emw.events.model;

import lombok.Data;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initiator_id")
    private Long initiator;

    private String annotation;

    @Column(name = "category_id")
    private Long category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    private LocalDateTime eventDate;

    private Point location;

    private boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    private String title;

    @Enumerated(EnumType.STRING)
    private EventState state;

}
