package ru.practicum.ewm.requests.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.requests.RequestMapper;
import ru.practicum.ewm.requests.model.ParticipantRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class UserRequestController {
    private final UserRequestService requestService;

    @GetMapping("/requests")
    public List<ParticipantRequestDto> getRequestsByUserId(@PathVariable Long userId) {
        return requestService.getRequestsByUserId(userId)
                .stream()
                .map(RequestMapper::mapRequestToParticipantRequestDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/requests")
    public ParticipantRequestDto postNewRequest(@PathVariable Long userId,
                                                @RequestParam Long eventId) {
        return RequestMapper.mapRequestToParticipantRequestDto(requestService.postNewRequest(userId, eventId));
    }

    @PatchMapping("/requests/{reqId}/cancel")
    public ParticipantRequestDto cancel(@PathVariable Long userId,
                                        @PathVariable Long reqId) {
        return RequestMapper.mapRequestToParticipantRequestDto(requestService.cancelRequest(userId, reqId));
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipantRequestDto> getRequestByEventId(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        return requestService.getByEvent(userId, eventId)
                .stream()
                .map(RequestMapper::mapRequestToParticipantRequestDto)
                .collect(Collectors.toList());
    }
}
