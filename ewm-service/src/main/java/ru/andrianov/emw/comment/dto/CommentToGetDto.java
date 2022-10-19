package ru.andrianov.emw.comment.dto;

import lombok.Data;
import ru.andrianov.emw.comment.model.CommentState;
import ru.andrianov.emw.events.dto.EventToCommentDto;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

import java.time.LocalDateTime;

@Data
public class CommentToGetDto {

    private Long id;

    private String text;

    private UserInitiatorDto owner;

    private EventToCommentDto event;

    private LocalDateTime created;

    private CommentState commentState;

}
