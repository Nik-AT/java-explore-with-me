package ru.practicum.ewm.requests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "requests")
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private Long requestorId;
    private Long eventId;
    private LocalDateTime createdOn;
    @Enumerated(EnumType.STRING)
    private RequestState state;

    public Request(long userId, long eventId, RequestState state) {
        this.requestorId = userId;
        this.eventId = eventId;
        this.createdOn = LocalDateTime.now();
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(requestId, request.requestId) &&
                Objects.equals(eventId, request.eventId) &&
                Objects.equals(requestorId, request.requestorId) &&
                Objects.equals(createdOn, request.createdOn) && state == request.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, eventId, requestorId, createdOn, state);
    }
}
