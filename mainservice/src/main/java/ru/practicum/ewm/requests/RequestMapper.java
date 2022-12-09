package ru.practicum.ewm.requests;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.requests.model.ParticipantRequestDto;
import ru.practicum.ewm.requests.model.Request;

@Component
public class RequestMapper {
    public static ParticipantRequestDto mapRequestToParticipantRequestDto(Request request) {
        return ParticipantRequestDto.builder()
                .id(request.getRequestId())
                .event(request.getEventId())
                .created(request.getCreatedOn())
                .requester(request.getRequestId())
                .status(request.getState()).build();
    }
}
