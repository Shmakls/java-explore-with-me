package ru.andrianov.emw.comment.exception;

public class CommentAlreadyExistException extends RuntimeException {

    public CommentAlreadyExistException(String message) {
        super(message);
    }
}
