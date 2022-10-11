package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.SetterParamsToCommentService;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.comment.exception.WrongCommentStateException;
import ru.andrianov.emw.comment.mapper.CommentMapper;
import ru.andrianov.emw.comment.model.Comment;
import ru.andrianov.emw.comment.model.CommentState;
import ru.andrianov.emw.comment.service.AdminCommentService;
import ru.andrianov.emw.comment.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentService commentService;

    private final SetterParamsToCommentService setterParamsToCommentService;

    @Override
    public List<CommentToGetDto> getCommentsToModerateByPages(Integer from, Integer size) {

        return commentService.getCommentsToModerateByPages(from, size)
                .stream()
                .map(CommentMapper::toGetDto)
                .peek(setterParamsToCommentService::setOwnerAndEventToComment)
                .collect(Collectors.toList());

    }

    @Override
    public CommentToGetDto publishCommentByAdmin(Long commentId) {

        Comment comment = commentService.getCommentById(commentId);

        if (comment.getCommentState() != CommentState.PENDING) {
            log.error("AdminCommentService.publicCommentByAdmin: comment state should be PENDING, not {}",
                    comment.getCommentState());
            throw new WrongCommentStateException("wrong comment state");
        }

        comment.setCommentState(CommentState.PUBLISHED);
        comment = commentService.updateComment(comment);

        return setterParamsToCommentService.setOwnerAndEventToComment(CommentMapper.toGetDto(comment));
    }

    @Override
    public CommentToGetDto rejectCommentByAdmin(Long commentId) {

        Comment comment = commentService.getCommentById(commentId);

        if (comment.getCommentState() != CommentState.PENDING) {
            log.error("AdminCommentService.rejectCommentByAdmin: comment state should be PENDING, not {}",
                    comment.getCommentState());
            throw new WrongCommentStateException("wrong comment state");
        }

        comment.setCommentState(CommentState.REJECTED);
        comment = commentService.updateComment(comment);

        return setterParamsToCommentService.setOwnerAndEventToComment(CommentMapper.toGetDto(comment));

    }
}
