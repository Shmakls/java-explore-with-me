package ru.andrianov.emw.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.andrianov.emw.events.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventToCreateDto {

    private Long id;

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
    private boolean paid;

    @NotNull
    private Long participantLimit;

    @NotNull
    private boolean requestModeration;

    @NotNull
    @NotEmpty
    private String title;

}
