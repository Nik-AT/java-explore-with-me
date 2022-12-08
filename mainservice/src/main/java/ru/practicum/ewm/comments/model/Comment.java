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
    private Long comment_id;

    private Long user_id;
    private String content;
    private Long event_id;
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!o.getClass().equals(this.getClass())) return false;
        Comment c = (Comment) o;
        return this.content.equals(c.getContent())
                && this.comment_id.equals(c.getComment_id())
                && this.event_id.equals(c.getEvent_id())
                && this.timestamp.equals(c.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.comment_id, this.comment_id, this.event_id, this.timestamp);
    }
}
