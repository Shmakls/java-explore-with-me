package ru.andrianov.emw.users.model;

import lombok.*;
import ru.andrianov.emw.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "initiator")
    private List<Event> events;

}
