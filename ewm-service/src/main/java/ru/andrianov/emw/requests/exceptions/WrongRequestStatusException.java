package ru.andrianov.emw.requests.exceptions;

public class WrongRequestStatusException extends RuntimeException {

    public WrongRequestStatusException(String message) {
        super(message);
    }
}
