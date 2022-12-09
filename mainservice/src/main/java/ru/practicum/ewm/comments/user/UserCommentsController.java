package ru.practicum.ewm.comments.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.model.CommentDto;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class UserCommentsController {
    private final UserCommentsService commentsService;

    @PostMapping("/events/{eventId}/comments")
    public CommentDto postNewComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody CommentDto commentDto) {
        return commentsService.addNewComment(userId, eventId, commentDto);
    }

    @PatchMapping("/events/{eventId}/comments")
    public CommentDto changeComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody CommentDto commentDto) {
        return commentsService.changeComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentsService.deleteComment(userId, commentId);
    }
}
