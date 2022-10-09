package ru.andrianov.emw.comment.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", length = 1000, nullable = false)
    private String text;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "event_id")
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private CommentState commentState;

    private LocalDateTime created = LocalDateTime.now();

}
