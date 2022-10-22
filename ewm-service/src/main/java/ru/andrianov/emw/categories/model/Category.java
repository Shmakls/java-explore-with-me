package ru.andrianov.emw.categories.model;

import lombok.Data;
import ru.andrianov.emw.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "categories", schema = "public")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Event> events;

}
