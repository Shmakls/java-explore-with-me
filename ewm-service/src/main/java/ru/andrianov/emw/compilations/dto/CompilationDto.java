package ru.andrianov.emw.compilations.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationDto {

    private Long id;

    private List<Long> events;

    @NotNull(message = "pinned should be not null")
    private boolean pinned;

    @NotEmpty(message = "title should be not empty")
    @NotNull(message = "title should be not null")
    private String title;

}
