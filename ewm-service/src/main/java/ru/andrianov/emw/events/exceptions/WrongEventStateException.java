package ru.andrianov.emw.events.exceptions;

public class WrongEventStateException extends RuntimeException {

    public WrongEventStateException(String message) {
        super(message);
    }
}
