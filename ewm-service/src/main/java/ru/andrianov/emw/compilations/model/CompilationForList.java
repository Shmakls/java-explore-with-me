package ru.andrianov.emw.compilations.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "compilation_list", schema = "public")
public class CompilationForList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "compilation_id")
    private Long compilationId;

    @Column(name = "event_id")
    private Long eventId;

}
