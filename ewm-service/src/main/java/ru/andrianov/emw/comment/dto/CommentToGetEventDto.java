package ru.andrianov.emw.comment.dto;

import lombok.Data;
import ru.andrianov.emw.comment.model.CommentState;

import java.time.LocalDateTime;

@Data
public class CommentToGetEventDto {

    private String text;

    private CommentState state;

    private LocalDateTime created;

}
