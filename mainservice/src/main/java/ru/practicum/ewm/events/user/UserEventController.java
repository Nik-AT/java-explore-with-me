package ru.practicum.ewm.events.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventMapper;
import ru.practicum.ewm.events.model.dto.EventDto;
import ru.practicum.ewm.events.model.dto.EventShortDto;
import ru.practicum.ewm.events.model.dto.NewEventDto;
import ru.practicum.ewm.events.model.dto.UpdateEventRequest;
import ru.practicum.ewm.requests.RequestMapper;
import ru.practicum.ewm.requests.model.ParticipantRequestDto;
import ru.practicum.ewm.requests.model.RequestState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class UserEventController {
    private final UserEventService userEventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventShortDto> getAllByUser(@PathVariable Long userId,
                                            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Valid @Positive @RequestParam(defaultValue = "10") int size) {

        return userEventService.getAllByUser(userId, from, size)
                .stream()
                .map(eventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public EventDto postNewEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        Event event = eventMapper.dtoToEvent(userId, newEventDto);
        return eventMapper.mapEventToDto(userEventService.postEvent(event));
    }

    @PatchMapping
    public EventDto changeEvent(@PathVariable Long userId,
                                @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        Event event = eventMapper.mapUpdateRequestToEvent(userId, updateEventRequest);
        return eventMapper.mapEventToDto(userEventService.changeEvent(event));
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        eventMapper.getUserById(userId);
        return eventMapper.mapEventToDto(userEventService.getEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventDto canselEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        eventMapper.getUserById(userId);
        return eventMapper.mapEventToDto(userEventService.cancelEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) {
        return RequestMapper.mapRequestToParticipantRequestDto(
                userEventService.handleEventRequest(userId, eventId, reqId, RequestState.REJECTED)
        );
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) {
        return RequestMapper.mapRequestToParticipantRequestDto(
                userEventService.handleEventRequest(userId, eventId, reqId, RequestState.CONFIRMED)
        );
    }
}
