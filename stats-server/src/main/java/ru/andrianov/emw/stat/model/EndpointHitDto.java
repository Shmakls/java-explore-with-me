package ru.andrianov.emw.stat.model;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class EndpointHitDto {

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
    private String timestamp;

}
