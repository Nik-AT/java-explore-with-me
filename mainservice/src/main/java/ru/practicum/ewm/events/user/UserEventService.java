package ru.practicum.ewm.events.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventState;
import ru.practicum.ewm.exceptions.ForbiddenOperationException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.requests.RequestRepository;
import ru.practicum.ewm.requests.model.Request;
import ru.practicum.ewm.requests.model.RequestState;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public List<Event> getAllByUser(Long userId, int from, int size) {
        return eventRepository.findByUserWithPagination(userId, from, size);
    }

    public Event postEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public Event changeEvent(Event event) {
        if (!event.getState().equals(EventState.PENDING))
            throw new ForbiddenOperationException("Wrong event state!");

        return eventRepository.save(event);
    }

    public Event getEventById(Long userId, Long eventId) {
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) throw new ModelNotFoundException("Event not found!");

        Event event = maybeEvent.get();

        if (event.getInitiatorId().equals(userId)) return event;
        else throw new ForbiddenOperationException("You're not initiator!");
    }

    @Transactional
    public Event cancelEvent(Long userId, Long eventId) {
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) throw new ModelNotFoundException("Event not found!");

        Event event = maybeEvent.get();

        if (!event.getInitiatorId().equals(userId))
            throw new ForbiddenOperationException("You're not initiator!");

        if (event.getState().equals(EventState.CANCELED))
            throw new ForbiddenOperationException("Event is already canceled!");
        eventRepository.setCanceled(EventState.CANCELED, eventId);
        event.setState(EventState.CANCELED);
        return event;
    }

    @Transactional
    public Request handleEventRequest(Long userId, Long eventId, Long reqId, RequestState state) {
        Optional<Request> optionalRequest = requestRepository.findById(reqId);
        if (optionalRequest.isEmpty()) throw new ModelNotFoundException("Request not found");
        Request request = optionalRequest.get();
        if (!request.getEventId().equals(eventId))
            throw new ForbiddenOperationException("Request doesnt belong to event!");
        getEventById(userId, eventId);
        request.setState(state);
        return requestRepository.save(request);
    }
}
