package ru.andrianov.emw.comment.exception;

public class WrongCommentStateException extends RuntimeException {

    public WrongCommentStateException(String message) {
        super(message);
    }
}
