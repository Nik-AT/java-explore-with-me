package ru.practicum.ewm.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {

    Integer countByUri(String uri);

    @Query(value = "select new ru.practicum.ewm.model.ViewStats(e.app, e.uri, count(e.app)) " +
            "from EndpointHit e where e.timestamp > ?1 and  e.timestamp <?2 group by e.app, e.uri")
    List<ViewStats> getNotUniqueViews(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.ewm.model.ViewStats(e.app, e.uri, count(e.app)) " +
            "from EndpointHit e where e.timestamp > ?1 and  e.timestamp <?2 group by e.app, e.uri, e.ip")
    List<ViewStats> getUniqueViews(LocalDateTime start, LocalDateTime end);
}
