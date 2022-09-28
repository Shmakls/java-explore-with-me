package ru.andrianov.emw.events.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EndpointHit {

    private Long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;

}
