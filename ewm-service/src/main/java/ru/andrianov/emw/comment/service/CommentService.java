package ru.andrianov.emw.comment.service;
import ru.andrianov.emw.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment getCommentById(Long commentId);

    Comment updateComment(Comment comment);

    List<Comment> getCommentsToModerateByPages(Integer from, Integer size);

    Optional<Comment> findCommentByOwnerIdAndEventId(Long ownerId, Long eventId);

    Comment addNewComment(Comment comment);

    List<Comment> getCommentsByOwnerIdByTime(Long userId, LocalDateTime start, LocalDateTime end,
                                             Integer from, Integer size);

    List<Comment> getCommentsByEventId(Long eventId,Integer from, Integer size);

    void deleteCommentById(Long commentId);
}
