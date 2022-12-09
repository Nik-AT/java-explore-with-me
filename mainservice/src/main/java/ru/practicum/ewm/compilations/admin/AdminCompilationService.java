package ru.practicum.ewm.compilations.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilations.CompilationMapper;
import ru.practicum.ewm.compilations.CompilationRepository;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.CompilationDto;
import ru.practicum.ewm.compilations.model.NewCompilationDto;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.exceptions.ForbiddenOperationException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AdminCompilationService {

    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    public CompilationDto postNew(NewCompilationDto newDto) {
        Compilation compilation = mapper.mapNewCompilationDtoToCompilation(newDto);
        return mapper.mapToDto(repository.save(compilation));
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) repository.deleteById(id);
        else throw new ModelNotFoundException("Compilation not found!");
    }

    @Transactional
    public void deleteOrAddEvent(Long compId, Long eventId, boolean shouldDelete) {
        Optional<Compilation> optionalComp = repository.findById(compId);
        if (optionalComp.isEmpty()) throw new ModelNotFoundException("Compilation not found!");
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) throw new ModelNotFoundException("Event not found!");

        Compilation compilation = optionalComp.get();
        Set<Event> compEvents = compilation.getEvents();

        if (shouldDelete) compEvents.remove(optionalEvent.get());
        else compEvents.add(optionalEvent.get());
        compilation.setEvents(compEvents);

        repository.save(compilation);
    }

    @Transactional
    public void pinOrUnpinComp(Long compId, boolean pined) {
        Optional<Compilation> optionalComp = repository.findById(compId);
        if (optionalComp.isEmpty()) throw new ModelNotFoundException("Compilation not found!");
        Compilation compilation = optionalComp.get();
        if (compilation.isPinned() == pined)
            throw new ForbiddenOperationException("Compilation already pinned or unpinned!");
        compilation.setPinned(pined);
        repository.save(compilation);
    }

}