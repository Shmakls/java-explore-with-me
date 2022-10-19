package ru.andrianov.emw.business.service.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.business.helper.Checker;
import ru.andrianov.emw.business.helper.DateTimeStringConverter;
import ru.andrianov.emw.business.helper.SetterParamsToCommentService;
import ru.andrianov.emw.comment.dto.CommentToCreateDto;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.comment.exception.CommentAlreadyExistException;
import ru.andrianov.emw.comment.mapper.CommentMapper;
import ru.andrianov.emw.comment.model.Comment;
import ru.andrianov.emw.comment.model.CommentState;
import ru.andrianov.emw.comment.service.CommentService;
import ru.andrianov.emw.comment.service.PrivateCommentService;
import ru.andrianov.emw.events.exceptions.WrongEventStateException;
import ru.andrianov.emw.events.model.EventState;
import ru.andrianov.emw.events.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentService commentService;

    private final EventService eventService;

    private final SetterParamsToCommentService setterParamsToCommentService;

    private final Checker checker;

    @Override
    public CommentToGetDto addNewComment(Long userId, CommentToCreateDto commentToCreateDto) {

        checker.userExistChecker(userId);

        checker.eventExistChecker(commentToCreateDto.getEventId());

        if (commentService.findCommentByOwnerIdAndEventId(userId, commentToCreateDto.getEventId()).isPresent()) {
            log.error("PrivateCommentService.addNewComment: user with id={} already added comment to event " +
                    "with id={}", userId, commentToCreateDto.getEventId());
            throw new CommentAlreadyExistException("comment already exist");
        }

        if (!(eventService.getEventById(commentToCreateDto.getEventId()).getState() == EventState.PUBLISHED)) {
            log.error("PrivateCommentService.addNewComment: event with id={} not published", commentToCreateDto.getEventId());
            throw new WrongEventStateException("event not published");
        }

        Comment comment = CommentMapper.fromCreateDto(commentToCreateDto);

        comment.setCommentState(CommentState.PENDING);
        comment.setOwnerId(userId);

        comment = commentService.addNewComment(comment);

        return setterParamsToCommentService.setOwnerAndEventToComment(CommentMapper.toGetDto(comment));

    }

    @Override
    public CommentToGetDto updateComment(Long userId, Long commentId, String text) {

        checker.userExistChecker(userId);

        Comment comment = commentService.getCommentById(commentId);

        comment.setText(text);

        comment.setCommentState(CommentState.PENDING);

        comment = commentService.updateComment(comment);

        return setterParamsToCommentService.setOwnerAndEventToComment(CommentMapper.toGetDto(comment));

    }

    @Override
    public CommentToGetDto getCommentById(Long userId, Long commentId) {

        checker.userExistChecker(userId);

        return setterParamsToCommentService
                .setOwnerAndEventToComment(CommentMapper.toGetDto(commentService.getCommentById(commentId)));
    }

    @Override
    public List<CommentToGetDto> getCommentsByUserId(Long userId, String startTime, String endTime,
                                                     Integer from, Integer size) {

        checker.userExistChecker(userId);

        LocalDateTime start = DateTimeStringConverter.fromFormattedString(startTime);
        LocalDateTime end = DateTimeStringConverter.fromFormattedString(endTime);

        return commentService.getCommentsByOwnerIdByTime(userId, start, end, from, size)
                .stream()
                .map(CommentMapper::toGetDto)
                .peek(setterParamsToCommentService::setOwnerAndEventToComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentToGetDto> getCommentsByEventId(Long userId, Long eventId, Integer from, Integer size) {

        checker.userExistChecker(userId);
        checker.eventExistChecker(eventId);

        return commentService.getCommentsByEventId(eventId, from, size)
                .stream()
                .map(CommentMapper::toGetDto)
                .peek(setterParamsToCommentService::setOwnerAndEventToComment)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteCommentByIdByOwner(Long userId, Long commentId) {

        checker.userExistChecker(userId);

        commentService.deleteCommentById(commentId);

    }

}
