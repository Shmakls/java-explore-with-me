package ru.andrianov.emw.comment.service;

import ru.andrianov.emw.comment.dto.CommentToGetDto;

import java.util.List;

public interface AdminCommentService {
    List<CommentToGetDto> getCommentsToModerateByPages(Integer from, Integer size);

    CommentToGetDto publishCommentByAdmin(Long commentId);

    CommentToGetDto rejectCommentByAdmin(Long commentId);
}
