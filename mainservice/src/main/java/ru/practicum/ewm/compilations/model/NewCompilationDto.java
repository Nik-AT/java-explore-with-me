package ru.practicum.ewm.compilations.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {
    private Long id;
    @NotNull
    @Length(min = 5, max = 150, message = "Min length = 5, max = 150 characters")
    private String title;
    private boolean pinned = false;
    private List<Long> events;
}
