package ru.andrianov.emw.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.andrianov.emw.events.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventToUpdateByAdminDto {

    private Long eventId;

    @NotNull
    @NotEmpty
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @NotEmpty
    private String description;

    private LocalDateTime createdOn = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;

    @NotNull
    private Long participantLimit;

    @NotNull
    private Boolean requestModeration;

    @NotNull
    @NotEmpty
    private String title;

}
