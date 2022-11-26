package ru.practicum.ewm.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select * from EVENTS where PAID=?1 and STATE=?2 and EVENT_DATE > ?3 order by EVENT_DATE",
            nativeQuery = true)
    List<Event> findByParams(boolean paid, String state, LocalDateTime start);

    @Query(value = "select * from events where initiator_Id=?1 limit ?3 offset ?2", nativeQuery = true)
    List<Event> findByUserWithPagination(Long userId, int from, int size);

    @Modifying
    @Query("update Event e set e.state = ?1 where e.eventId = ?2")
    void setCanceled(EventState state, Long eventId);

    @Query("select e from Event e where e.initiatorId in ?1 and e.categoryId in ?2 and e.state in ?3 " +
            "and e.eventDate > ?4 and e.eventDate < ?5")
    List<Event> findByParams(Long[] ids, Long[] catIds, EventState[] states,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd);
}
