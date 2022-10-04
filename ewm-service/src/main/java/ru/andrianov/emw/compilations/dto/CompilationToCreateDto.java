package ru.andrianov.emw.compilations.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompilationToCreateDto {

    private boolean pinned;

    @NotBlank
    @Length(max = 200)
    private String title;

    private List<Long> events;

}
