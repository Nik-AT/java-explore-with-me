package ru.practicum.ewm.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class ApiError {
    private final HttpStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> errors;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}