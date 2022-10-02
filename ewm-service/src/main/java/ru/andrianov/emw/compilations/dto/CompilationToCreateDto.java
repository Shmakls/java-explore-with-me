package ru.andrianov.emw.compilations.dto;

import lombok.Data;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationToCreateDto {

    @NotNull(message = "pinned status should be not null")
    private boolean pinned;

    @NotNull(message = "compilation title should be not null")
    @NotEmpty(message = "compilation title should be not empty")
    private String title;

    @NotNull
    @OneToMany
    private List<Long> events;

}
