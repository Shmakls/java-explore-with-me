package ru.andrianov.emw.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.events.model.Location;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventToGetDto {

    private Long id;

    private String annotation;

    private Category category;

    private Long confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserInitiatorDto initiator;

    private Location location;

    private boolean paid;

    private Long participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private String state;

    private String title;


}
