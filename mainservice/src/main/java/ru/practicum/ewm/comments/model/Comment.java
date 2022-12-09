package ru.practicum.ewm.comments.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long userId;
    private String content;
    private Long eventId;
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!o.getClass().equals(this.getClass())) return false;
        Comment c = (Comment) o;
        return this.content.equals(c.getContent())
                && this.commentId.equals(c.getCommentId())
                && this.eventId.equals(c.getEventId())
                && this.timestamp.equals(c.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.commentId, this.commentId, this.eventId, this.timestamp);
    }
}
