package ru.andrianov.emw.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.andrianov.emw.events.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventToCreateDto {

    private Long id;

    @NotBlank
    @Length(max = 500)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Length(max = 2000)
    private String description;

    private LocalDateTime createdOn = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private boolean paid;

    @PositiveOrZero
    private long participantLimit = 0;

    private boolean requestModeration;

    @NotBlank
    @Length(max = 200)
    private String title;

}
