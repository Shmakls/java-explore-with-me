package ru.andrianov.emw.comment.service;

import ru.andrianov.emw.comment.dto.CommentToCreateDto;
import ru.andrianov.emw.comment.dto.CommentToGetDto;

import java.util.List;

public interface PrivateCommentService {
    CommentToGetDto addNewComment(Long userId, CommentToCreateDto commentToCreateDto);

    CommentToGetDto updateComment(Long userId, Long commentId, String text);

    CommentToGetDto getCommentById(Long userId, Long commentId);

    List<CommentToGetDto> getCommentsByUserId(Long userId, String startTime, String endTime, Integer from, Integer size);

    List<CommentToGetDto> getCommentsByEventId(Long userId, Long eventId, Integer from, Integer size);

    void deleteCommentByIdByOwner(Long userId, Long commentId);
}
