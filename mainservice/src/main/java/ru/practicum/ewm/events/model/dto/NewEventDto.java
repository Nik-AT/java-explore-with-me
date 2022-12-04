package ru.practicum.ewm.events.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @Length(min = 5, max = 250)
    private String title;
    @NotBlank
    @Length(min = 10, max = 1000)
    private String annotation;
    @NotBlank
    @Length(min = 20, max = 2000)
    private String description;
    @NotNull
    private Long category;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Integer participantLimit;
    private boolean paid;
    private boolean requestModeration;
    @NotNull
    private LocationDto location;
}
