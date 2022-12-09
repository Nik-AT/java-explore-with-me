package ru.practicum.ewm.api;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.HitDto;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatisticsController {
    private final StatisticsService service;

    @GetMapping("/hit")
    public Integer getViews(@RequestParam String uri) {
        return service.countViewsByUri(uri);
    }

    @PostMapping("/hit")
    public void saveHit(@RequestBody HitDto hitDto) {
        service.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return service.getStats(start, end, List.of(uris), unique);
    }
}
