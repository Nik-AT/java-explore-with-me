package ru.practicum.ewm.events.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventMapper;
import ru.practicum.ewm.events.model.EventState;
import ru.practicum.ewm.events.model.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.events.model.dto.EventDto;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.util.Pager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final EventMapper eventMapper;

    private final Pager<EventDto> pager;

    @PutMapping("/{eventId}")
    public EventDto putEvent(@PathVariable Long eventId,
                             @RequestBody AdminUpdateEventRequest request) {
        Event event = eventMapper.mapAdminRequestToEvent(eventId, request);
        return eventMapper.mapEventToDto(adminEventService.putEvent(event));
    }

    @GetMapping
    public List<EventDto> getByParams(@RequestParam(required = false) Long[] users,
                                      @RequestParam(required = false) EventState[] states,
                                      @RequestParam(required = false) Long[] categories,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        List<EventDto> events = adminEventService.getByParams(users, states, categories, rangeStart, rangeEnd)
                .stream()
                .map(eventMapper::mapEventToDto)
                .collect(Collectors.toList());
        return pager.getPage(events, from, size).getContent();
    }

    @PatchMapping("/{eventId}/publish")
    public EventDto publish(@PathVariable String eventId) {
        log.info("/admin/events/{eventId}/publish................................eventId = {}", eventId);
        try {
            return eventMapper.mapEventToDto(adminEventService.setState(Long.parseLong(eventId), EventState.PUBLISHED));
        } catch (NumberFormatException e) {
            throw new ModelNotFoundException("Event not found!");
        }
    }

    @PatchMapping("/{eventId}/reject")
    public EventDto reject(@PathVariable String eventId) {
        log.info("/admin/events/{eventId}/reject................................eventId = {}", eventId);
        try {
            return eventMapper.mapEventToDto(adminEventService.setState(Long.parseLong(eventId), EventState.CANCELED));
        } catch (NumberFormatException e) {
            throw new ModelNotFoundException("Event not found!");
        }
    }


}
