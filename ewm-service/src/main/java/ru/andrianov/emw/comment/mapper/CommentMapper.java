package ru.andrianov.emw.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.comment.dto.CommentToCreateDto;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.comment.dto.CommentToGetEventDto;
import ru.andrianov.emw.comment.model.Comment;
import ru.andrianov.emw.events.dto.EventToCommentDto;
import ru.andrianov.emw.users.dto.UserInitiatorDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment fromCreateDto(CommentToCreateDto commentToCreateDto) {

        Comment comment = new Comment();

        comment.setText(commentToCreateDto.getText());
        comment.setEventId(commentToCreateDto.getEventId());

        return comment;

    }

    public static CommentToGetDto toGetDto(Comment comment) {

        CommentToGetDto commentToGetDto = new CommentToGetDto();

        commentToGetDto.setId(comment.getId());
        commentToGetDto.setOwner(new UserInitiatorDto(comment.getOwnerId()));
        commentToGetDto.setEvent(new EventToCommentDto(comment.getEventId()));
        commentToGetDto.setText(comment.getText());
        commentToGetDto.setCreated(comment.getCreated());

        return commentToGetDto;

    }

    public static CommentToGetEventDto toGetEventDto(Comment comment) {

        CommentToGetEventDto commentToGetEventDto = new CommentToGetEventDto();

        commentToGetEventDto.setText(comment.getText());
        commentToGetEventDto.setState(comment.getCommentState());
        commentToGetEventDto.setCreated(comment.getCreated());

        return commentToGetEventDto;

    }

}
