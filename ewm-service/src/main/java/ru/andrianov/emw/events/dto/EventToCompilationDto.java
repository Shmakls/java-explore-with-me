package ru.andrianov.emw.events.dto;

import lombok.Data;
import ru.andrianov.emw.categories.dto.CategoryDto;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

import java.time.LocalDateTime;

@Data
public class EventToCompilationDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private LocalDateTime eventDate;

    private UserInitiatorDto initiator;

    private boolean paid;

    private String title;

    private Long views;

}
