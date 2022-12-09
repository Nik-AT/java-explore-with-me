package ru.practicum.ewm.events.forAll;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.EventClient;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventMapper;
import ru.practicum.ewm.events.model.SortType;
import ru.practicum.ewm.events.model.dto.EventDto;
import ru.practicum.ewm.events.model.dto.EventShortDto;
import ru.practicum.ewm.requests.model.Request;
import ru.practicum.ewm.util.Pager;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class PublicEventController {
    private final PublicEventService eventService;
    private final EventMapper eventMapper;
    private final Pager<EventShortDto> pager;
    private final EventClient eventClient;

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        eventClient.addRequest(httpServletRequest);
        return eventMapper.mapEventToDto(eventService.getEventById(eventId));
    }

    @GetMapping
    public List<EventShortDto> getAllByParamsWithPagination(@RequestParam(defaultValue = "")
                                                            String text,
                                                            @RequestParam(defaultValue = "0")
                                                            Long[] categories,
                                                            @RequestParam(defaultValue = "false")
                                                            boolean paid,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            LocalDateTime rangeStart,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            LocalDateTime rangeEnd,
                                                            @RequestParam(defaultValue = "true")
                                                            boolean onlyAvailable,
                                                            @RequestParam(defaultValue = "EVENT_DATE")
                                                            SortType sortType,
                                                            @Valid @PositiveOrZero @RequestParam(defaultValue = "0")
                                                            int from,
                                                            @Valid @Positive @RequestParam(defaultValue = "10")
                                                            int size,
                                                            HttpServletRequest httpServletRequest) {
        log.info("events................PUBLIC CONTROLLER......................PARAMS.................:\n" +
                "TEXT = {}\n" +
                "catIds = {}\n" +
                "paid = {}\n" +
                "START = {}\n" +
                "END = {}\n" +
                "available = {}\n" +
                "SORT = {}\n" +
                "FROM = {}\n" +
                "SIZE = {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortType, from, size);
        eventClient.addRequest(httpServletRequest);
        List<Event> events = eventService.getAllByParams(
                        text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd)
                .stream()
                .map(event -> {
                    if (event == null) return null;
                    if (onlyAvailable) {
                        List<Request> requests = eventMapper.getRequestsByEventId(event.getEventId());
                        if (requests.size() < event.getParticipantLimit()) return event;
                        else return null;
                    } else return event;
                }).collect(Collectors.toList());

        List<EventShortDto> sortedDtoList = eventMapper.sortList(events, sortType);
        if (sortedDtoList == null) return List.of();
        else return pager.getPage(sortedDtoList, from, size).getContent();
    }
}
