package ru.practicum.ewm.requests.user;

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
import ru.practicum.ewm.users.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<Request> getRequestsByUserId(Long userId) {
        return requestRepository.findAllByRequestor_id(userId);
    }

    public Request postNewRequest(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = getEventById(eventId);
        if (event.getInitiatorId().equals(userId))
            throw new ForbiddenOperationException("You are the initiator!");

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenOperationException("Event must be published!");

        long eventConfirmedRequests = requestRepository.getConfirmed(eventId);
        if (eventConfirmedRequests >= event.getParticipantLimit())
            throw new ForbiddenOperationException("Participant limit is over!");

        if (event.isRequestModeration()) {
            return requestRepository.save(new Request(userId, eventId, RequestState.PENDING));
        } else {
            return requestRepository.save(new Request(userId, eventId, RequestState.CONFIRMED));
        }
    }

    @Transactional
    public Request cancelRequest(Long userId, Long requestId) {
        checkUserById(userId);
        Request request = getRequestById(requestId);
        if (request.getState().equals(RequestState.CANCELED))
            throw new ForbiddenOperationException("Request is already canceled!");
        if (!request.getRequestorId().equals(userId))
            throw new ForbiddenOperationException("Only requestor can cancel request!");
        request.setState(RequestState.CANCELED);
        return requestRepository.save(request);
    }

    public List<Request> getByEvent(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiatorId().equals(userId))
            throw new ForbiddenOperationException("User must be initiator!");

        return requestRepository.findAllByEvent_id(eventId);
    }

    private void checkUserById(Long userId) {
        if (!userRepository.existsById(userId))
            throw new ModelNotFoundException("User not found");
    }

    private Event getEventById(Long eventId) {
        Optional<Event> mayBeEvent = eventRepository.findById(eventId);
        if (mayBeEvent.isEmpty())
            throw new ModelNotFoundException("Event not found!");
        else return mayBeEvent.get();
    }

    private Request getRequestById(long reqId) {
        Optional<Request> optionalRequest = requestRepository.findById(reqId);
        if (optionalRequest.isEmpty()) {
            throw new ModelNotFoundException("Request not found!");
        }
        return optionalRequest.get();
    }
}
