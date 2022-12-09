package ru.practicum.ewm.compilations.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.events.model.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
