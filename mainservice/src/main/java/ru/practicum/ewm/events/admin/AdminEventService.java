package ru.practicum.ewm.events.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventState;
import ru.practicum.ewm.exceptions.ForbiddenOperationException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

    public Event putEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getByParams(Long[] userIds,
                                   EventState[] states,
                                   Long[] categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd) {
        return eventRepository.findByParams(userIds, categories, states, rangeStart, rangeEnd);
    }

    @Transactional
    public Event setState(Long eventId, EventState state) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new ModelNotFoundException("Event not found!");

        Event event = optionalEvent.get();
        if (!event.getState().equals(EventState.PENDING))
            throw new ForbiddenOperationException("Event state must be pending!");

        event.setState(state);
        if (state == EventState.PUBLISHED)
            event.setPublished(LocalDateTime.now());

        return eventRepository.save(event);
    }
}
