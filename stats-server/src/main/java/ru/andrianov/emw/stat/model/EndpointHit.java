package ru.andrianov.emw.stat.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "endpoint_stat", schema = "public")

public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String app;

    @NotEmpty
    @NotNull
    private String uri;

    @NotNull
    @NotEmpty
    private String ip;

    @NotNull
    private LocalDateTime timestamp;

}
