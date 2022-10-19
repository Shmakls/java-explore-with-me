package ru.andrianov.emw.comment.model;

import java.util.Optional;

public enum CommentState {

    PENDING,
    PUBLISHED,
    REJECTED,
    CANCELED;

    public static Optional<CommentState> from(String stringState) {

        for (CommentState commentState : values()) {
            if (commentState.name().equalsIgnoreCase(stringState)) {
                return Optional.of(commentState);
            }
        }
        return Optional.empty();
    }



}
