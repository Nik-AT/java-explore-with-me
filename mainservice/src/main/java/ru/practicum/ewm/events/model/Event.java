package ru.practicum.ewm.events.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    private String title;
    private String annotation;
    private String description;
    private Long categoryId;
    private Long initiatorId;
    private LocalDateTime createdOn;
    private boolean paid;
    private Long participantLimit;
    private LocalDateTime eventDate;
    private double lat;
    private double lon;
    private LocalDateTime published;
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId != null && Objects.equals(eventId, event.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
