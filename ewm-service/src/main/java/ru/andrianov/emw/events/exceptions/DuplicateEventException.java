package ru.andrianov.emw.events.exceptions;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String message) {
        super(message);
    }
}
