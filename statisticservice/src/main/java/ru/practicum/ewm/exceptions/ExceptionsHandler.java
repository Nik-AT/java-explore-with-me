package ru.practicum.ewm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("No request parameters specified!")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRuntimeException(final RuntimeException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Something went wrong!")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
