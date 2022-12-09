package ru.practicum.ewm.exceptions;

public class ModelAlreadyExistsException extends RuntimeException {
    public ModelAlreadyExistsException(String message) {
        super(message);
    }
}
