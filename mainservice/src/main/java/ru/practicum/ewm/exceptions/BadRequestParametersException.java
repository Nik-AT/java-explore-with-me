package ru.practicum.ewm.exceptions;

public class BadRequestParametersException extends RuntimeException {

    public BadRequestParametersException(String message) {
        super(message);
    }
}
