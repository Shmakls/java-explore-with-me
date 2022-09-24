package ru.andrianov.emw.compilations.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

}
