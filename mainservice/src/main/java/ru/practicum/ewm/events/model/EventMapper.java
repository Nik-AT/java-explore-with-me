package ru.practicum.ewm.events.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.categories.CategoryRepository;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.dto.CategoryDto;
import ru.practicum.ewm.events.EventClient;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.dto.*;
import ru.practicum.ewm.exceptions.ForbiddenOperationException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.requests.RequestRepository;
import ru.practicum.ewm.requests.model.Request;
import ru.practicum.ewm.users.UserRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventMapper {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventClient client;

    public Event dtoToEvent(Long userId, NewEventDto newEventDto) {
        if (!categoryRepository.existsById(newEventDto.getCategory()))
            throw new ModelNotFoundException("Category not found!");

        if (!userRepository.existsById(userId))
            throw new ModelNotFoundException("User not found!");

        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setCategoryId(newEventDto.getCategory());
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiatorId(userId);
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit().longValue());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setState(EventState.PENDING);
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        return event;
    }

    public EventDto mapEventToDto(Event event) {
        Category category = getCategoryById(event.getCategoryId());
        User user = getUserById(event.getInitiatorId());
        String uri = "/events/" + event.getEventId();
        Integer views = (Integer) client.getViews(uri);

        return EventDto.builder()
                .id(event.getEventId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .location(new LocationDto(event.getLat(), event.getLon()))
                .confirmedRequests(requestRepository.getConfirmed(event.getEventId()))
                .category(new CategoryDto(category.getCategoryId(), category.getCategoryName()))
                .initiator(new UserShortDto(user.getUserId(), user.getName()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublished())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .views(views.longValue())
                .build();
    }

    public EventShortDto mapToShortDto(Event event) {
        Category category = getCategoryById(event.getCategoryId());
        int confirmedRequests = requestRepository.getConfirmed(event.getEventId());
        User user = getUserById(event.getInitiatorId());
        String uri = "/events/" + event.getEventId();
        Integer views = (Integer) client.getViews(uri);

        return EventShortDto.builder()
                .id(event.getEventId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(category.getCategoryId(), category.getCategoryName()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(user.getUserId(), user.getName()))
                .paid(event.isPaid())
                .views(views)
                .build();
    }

    public Event mapUpdateRequestToEvent(Long userId, UpdateEventRequest updateEventRequest) {
        getUserById(userId);
        Event event = getEventById(updateEventRequest.getEventId());
        if (!event.getInitiatorId().equals(userId))
            throw new ForbiddenOperationException("You can't edit this event!");

        setEventFields(
                event,
                updateEventRequest.getTitle(),
                updateEventRequest.getAnnotation(),
                updateEventRequest.getDescription(),
                updateEventRequest.getCategory(),
                updateEventRequest.getEventDate(),
                updateEventRequest.isPaid(),
                updateEventRequest.getParticipantLimit());
        return event;
    }

    public Event mapAdminRequestToEvent(Long eventId, AdminUpdateEventRequest request) {
        Event event = getEventById(eventId);
        setEventFields(
                event,
                request.getTitle(),
                request.getAnnotation(),
                request.getDescription(),
                request.getCategory(),
                request.getEventDate(),
                request.isPaid(),
                request.getParticipantLimit());
        event.setRequestModeration(request.isRequestModeration());

        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        return event;
    }

    private void setEventFields(
            Event event,
            String title,
            String annotation,
            String description,
            long category,
            LocalDateTime eventDate,
            boolean paid,
            Integer participantLimit) {
        event.setTitle(title);
        event.setAnnotation(annotation);
        event.setDescription(description);
        getCategoryById(category);
        event.setCategoryId(category);
        event.setEventDate(eventDate);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit.longValue());
    }

    public List<Request> getRequestsByEventId(Long id) {
        return requestRepository.findAllByEvent_id(id);
    }

    public List<Event> mapEventIdsToEventList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return eventRepository.findAllById(ids);
    }

    public List<EventShortDto> sortList(List<Event> list, SortType sortType) {
        return list.stream()
                .map(this::mapToShortDto)
                .sorted((e1, e2) -> {
                    if (sortType.equals(SortType.VIEWS)) {
                        return e1.getViews().compareTo(e2.getViews());
                    } else {
                        return e1.getEventDate().compareTo(e2.getEventDate());
                    }
                })
                .collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) throw new ModelNotFoundException("Event not found!");
        else return maybeEvent.get();
    }

    private Category getCategoryById(Long catId) {
        Optional<Category> optionalCategory = categoryRepository.findById(catId);
        if (optionalCategory.isPresent()) return optionalCategory.get();
        else throw new ModelNotFoundException("Category not found!");
    }

    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) return optionalUser.get();
        else throw new ModelNotFoundException("User not found!");
    }
}
