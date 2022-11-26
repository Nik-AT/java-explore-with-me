package ru.practicum.ewm.compilations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.CompilationDto;
import ru.practicum.ewm.compilations.model.NewCompilationDto;
import ru.practicum.ewm.events.model.EventMapper;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public List<CompilationDto> mapCompilationList(List<Compilation> list) {
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CompilationDto mapToDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getCompilationId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.isPinned());
        dto.setEvents(compilation.getEvents().stream().map(eventMapper::mapToShortDto).collect(Collectors.toList()));
        return dto;
    }

    public Compilation mapNewCompilationDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setEvents(new HashSet<>(eventMapper.mapEventIdsToEventList(newCompilationDto.getEvents())));
        return compilation;
    }
}
