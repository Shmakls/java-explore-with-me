package ru.andrianov.emw.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.comment.model.Comment;
import ru.andrianov.emw.comment.model.CommentState;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByCommentState(CommentState state, Pageable pageable);

    Optional<Comment> findCommentByOwnerIdAndEventId(Long ownerId, Long eventId);

    Page<Comment> findCommentsByOwnerIdAndCreatedBetween(Long userId, LocalDateTime start, LocalDateTime end,
                                                         Pageable pageable);

    Page<Comment> findCommentsByEventId(Long eventId, Pageable pageable);

}
