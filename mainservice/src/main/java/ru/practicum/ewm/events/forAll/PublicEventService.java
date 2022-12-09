package ru.practicum.ewm.events.forAll;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventState;
import ru.practicum.ewm.exceptions.ModelNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;

    public Event getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) return optionalEvent.get();
        else throw new ModelNotFoundException("Event not found!");
    }

    public List<Event> getAllByParams(
            String text,
            Long[] categories,
            boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        log.info("events................PUBLIC CONTROLLER......................PARAMS.................:\n" +
                "TEXT = {}\n" +
                "catIds = {}\n" +
                "paid = {}\n" +
                "START = {}\n" +
                "END = {}\n", text, categories, paid, start, rangeEnd);
        return eventRepository.findByParams(paid, EventState.PUBLISHED.toString(), start)
                .stream()
                .map((event) -> filterByRangeEnd(event, rangeEnd))
                .map((event) -> filterByCatIds(event, List.of(categories)))
                .map((event) -> filterByText(event, text))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private Event filterByRangeEnd(Event event, LocalDateTime rangeEnd) {
        if (event == null) return null;
        if (rangeEnd == null) return event;
        else if (event.getEventDate().isBefore(rangeEnd)) return event;
        else return null;
    }

    private Event filterByCatIds(Event event, List<Long> catIds) {
        if (catIds == null) return event;
        if (event == null) return null;
        if (catIds.contains(event.getCategoryId())) return event;
        else return null;
    }

    private Event filterByText(Event event, String text) {
        if (event == null) return null;
        if (text == null || text.isEmpty()) return event;
        String search = text.toLowerCase().trim();
        String searchAnnotation = event.getAnnotation().toLowerCase().trim();
        String searchDescription = event.getDescription().toLowerCase().trim();
        if (searchAnnotation.contains(search) || searchDescription.contains(search)) return event;
        else return null;
    }
}
