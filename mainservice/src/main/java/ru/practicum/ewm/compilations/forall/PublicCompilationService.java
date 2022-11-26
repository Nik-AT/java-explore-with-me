package ru.practicum.ewm.compilations.forall;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilations.CompilationMapper;
import ru.practicum.ewm.compilations.CompilationRepository;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.CompilationDto;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.util.Pager;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicCompilationService {

    private final CompilationRepository repository;
    private final Pager<CompilationDto> pageCreator;
    private final CompilationMapper mapper;

    public List<CompilationDto> getAll(boolean pinned, int from, int size) {
        List<Compilation> found = repository.findAllByPinned(pinned);
        return pageCreator
                .getPage(mapper.mapCompilationList(found), from, size)
                .getContent();
    }

    public CompilationDto getById(Long compId) {
        Optional<Compilation> optionalComp = repository.findById(compId);
        if (optionalComp.isPresent())
            return mapper.mapToDto(optionalComp.get());
        else throw new ModelNotFoundException("Compilation not found");
    }
}
