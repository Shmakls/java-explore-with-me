package ru.andrianov.emw.business.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.comment.dto.CommentToGetDto;
import ru.andrianov.emw.events.service.EventService;
import ru.andrianov.emw.users.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SetterParamsToCommentService {

    private final UserService userService;

    private final EventService eventService;


    public CommentToGetDto setOwnerAndEventToComment(CommentToGetDto commentToGetDto) {

        commentToGetDto.getOwner().setName(userService.getUserNameById(commentToGetDto.getOwner().getId()));
        commentToGetDto.getEvent().setTitle(eventService.getEventTitleById(commentToGetDto.getEvent().getId()));

        return commentToGetDto;

    }

}
