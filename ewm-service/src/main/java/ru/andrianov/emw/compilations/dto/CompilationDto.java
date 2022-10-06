package ru.andrianov.emw.compilations.dto;

import lombok.Data;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import java.util.List;

@Data
public class CompilationDto {

    private Long id;

    private List<EventToCompilationDto> events;

    private boolean pinned;

    private String title;

}
