package ru.andrianov.emw.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.comment.service.AdminCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Slf4j
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @GetMapping
    public List<CommentToGetDto> getCommentsToModerateByPages(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                              @Positive @RequestParam(required = false, defaultValue = "20") Integer size) {

        log.info("AdminCommentController.getCommentToModerateByPages: received a request to get " +
                "comments to moderate");

        return adminCommentService.getCommentsToModerateByPages(from, size);

    }

    @PatchMapping("/{commentId}/publish")
    public CommentToGetDto publishCommentByAdmin(@PathVariable Long commentId) {

        log.info("AdminCommentController.publishCommentByAdmin: received a request to publish " +
                "comment with id={} by admin", commentId);

        return adminCommentService.publishCommentByAdmin(commentId);

    }

    @PatchMapping("/{commentId}/reject")
    public CommentToGetDto rejectCommentByAdmin(@PathVariable Long commentId) {

        log.info("AdminCommentController.rejectCommentByAdmin: received a request to reject " +
                "comment with id={} by admin", commentId);

        return adminCommentService.rejectCommentByAdmin(commentId);

    }

}
