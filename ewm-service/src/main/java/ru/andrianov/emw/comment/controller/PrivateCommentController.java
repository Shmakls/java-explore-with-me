package ru.andrianov.emw.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.categories.model.Create;
import ru.andrianov.emw.comment.dto.CommentToCreateDto;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.comment.service.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("users/{userId}/comments")
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @PostMapping
    public CommentToGetDto addNewComment(@PathVariable Long userId,
                                         @Validated(Create.class) CommentToCreateDto  commentToCreateDto) {

        log.info("PrivateCommentController.addNewComment: received a request to add new comment " +
                "to event with id={} by user with id={}", commentToCreateDto.getEventId(), userId);

        return privateCommentService.addNewComment(userId, commentToCreateDto);

    }

    @PatchMapping("/{commentId}")
    public CommentToGetDto updateComment(@PathVariable Long userId,
                                         @Valid @NotBlank String text, @PathVariable Long commentId) {

        log.info("PrivateCommentController.updateComment: received a request to update comment " +
                "with id={} by user with id={}", commentId, userId);

        return privateCommentService.updateComment(userId, commentId, text);

    }

    @GetMapping("/{commentId}")
    public CommentToGetDto getCommentById(@PathVariable Long userId, @PathVariable Long commentId) {

        log.info("PrivateCommentController.getCommentById: received a request comment with id={} " +
                "by user with id={}", commentId, userId);

        return privateCommentService.getCommentById(userId, commentId);

    }

    @GetMapping
    public List<CommentToGetDto> getCommentsByUserIdByPeriod(@PathVariable Long userId,
                                                             @RequestParam String startTime,
                                                             @RequestParam String endTime,
                                                             @PositiveOrZero @RequestParam Integer from,
                                                             @Positive Integer size) {

        log.info("PrivateCommentController.getCommentsByUserIdByPeriod: received a request to get user " +
                "comments with id={}", userId);

        return privateCommentService.getCommentsByUserId(userId, startTime, endTime, from, size);

    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentToGetDto> getCommentsByEventId(@PathVariable Long userId, @PathVariable Long eventId,
                                                      @PositiveOrZero @RequestParam Integer from,
                                                      @Positive Integer size) {

        log.info("PrivateCommentController.getCommentsByEventId: received a request to get comments " +
                "by event with id={} by user with id={}", eventId, userId);

        return privateCommentService.getCommentsByEventId(userId, eventId, from, size);

    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByIdByOwner(@PathVariable Long userId, @PathVariable Long commentId) {

        log.info("PrivateCommentController.deleteCommentByIdOwner: received a request to delete comment " +
                "with id={} by user with id={}", commentId, userId);

        privateCommentService.deleteCommentByIdByOwner(userId, commentId);

    }

}
