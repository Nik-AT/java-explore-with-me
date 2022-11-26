package ru.practicum.ewm.exceptions;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleModelNotFoundException(ModelNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                List.of(),
                "Object not found",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleModelAlreadyExistsException(ModelAlreadyExistsException e) {
        return new ApiError(
                HttpStatus.CONFLICT,
                List.of(),
                "Some fields aren't unique",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleOperationForbiddenException(ForbiddenOperationException e) {
        return new ApiError(
                HttpStatus.FORBIDDEN,
                List.of(),
                "Entity can not be changed!",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
        return new ApiError(HttpStatus.CONFLICT,
                List.of(
                        e.getLocalizedMessage(),
                        Objects.requireNonNull(e.getRootCause()).toString(),
                        e.getMostSpecificCause().toString()),
                "InvalidDataAccessResourceUsageException",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        String objectName = e.getObjectName();
        int errorCount = e.getErrorCount();
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                getErrors(e.getFieldErrors()),
                String.format("During [%s] validation found %s errors", objectName, errorCount),
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRuntimeException(final RuntimeException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(),
                e.getClass().getName(),
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    private List<String> getErrors(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add("Field: " + fieldError.getField() +
                    ". Error: " + fieldError.getDefaultMessage() +
                    ". Value: " + fieldError.getRejectedValue());
        }
        return errors;
    }
}
