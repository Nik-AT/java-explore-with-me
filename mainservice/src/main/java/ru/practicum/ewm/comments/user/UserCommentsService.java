package ru.practicum.ewm.comments.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.CommentRepository;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.model.CommentDto;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.exceptions.ForbiddenOperationException;
import ru.practicum.ewm.exceptions.ModelAlreadyExistsException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.requests.RequestRepository;
import ru.practicum.ewm.requests.model.Request;
import ru.practicum.ewm.requests.model.RequestState;
import ru.practicum.ewm.users.UserRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserCommentsService {
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CommentDto addNewComment(Long userId, Long eventId, CommentDto commentDto) {
        checkEventId(eventId);
        checkUserId(userId);
        checkRequest(eventId, userId);

        Comment comment = new Comment();
        comment.setUser_id(userId);
        comment.setContent(commentDto.getContent());
        comment.setEvent_id(eventId);
        comment.setTimestamp(LocalDateTime.now());

        try {
            return mapToDto(commentRepository.save(comment));
        } catch (DataIntegrityViolationException e) {
            throw new ModelAlreadyExistsException("Comment content must be unique!");
        }
    }

    public CommentDto changeComment(Long userId, Long eventId, CommentDto commentDto) {
        checkEventId(eventId);
        checkUserId(userId);
        checkRequest(eventId, userId);

        Comment comment = checkCommentId(commentDto.getId(), userId);
        if (commentDto.getTimestamp().minusHours(1L).isAfter(comment.getTimestamp()))
            throw new ForbiddenOperationException("Time for changing comment is over!");

        comment.setContent(commentDto.getContent());

        try {
            return mapToDto(commentRepository.save(comment));
        } catch (DataIntegrityViolationException e) {
            throw new ModelAlreadyExistsException("Comment content must be unique!");
        }
    }

    public void deleteComment(Long userId, Long commentId) {
        commentRepository.delete(checkCommentId(commentId, userId));
    }

    public List<CommentDto> getEventComments(Long eventId) {
        return commentRepository.findAllByEventId(eventId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private Comment checkCommentId(Long commentId, Long userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) throw new ModelNotFoundException("Comment not found!");
        Comment comment = optionalComment.get();
        if (!comment.getUser_id().equals(userId))
            throw new ForbiddenOperationException("User is not the author!");
        return comment;
    }

    private User checkUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ModelNotFoundException("User notfound!");
        return optionalUser.get();
    }

    private void checkEventId(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) throw new ModelNotFoundException("Event not found!");
    }

    private void checkRequest(Long eventId, Long userId) {
        Optional<Request> optionalRequest = requestRepository.findRequestByEvent_idAAndRequestor_id(eventId, userId);
        if (optionalRequest.isEmpty()) throw new ModelNotFoundException("Request not found!");
        if (!optionalRequest.get().getState().equals(RequestState.CONFIRMED))
            throw new ForbiddenOperationException("User wasn't a participant of this event!");
    }

    private CommentDto mapToDto(Comment comment) {
        User user = checkUserId(comment.getUser_id());
        return CommentDto.builder()
                .id(comment.getComment_id())
                .userShortDto(new UserShortDto(user.getUser_id(), user.getName()))
                .content(comment.getContent())
                .timestamp(comment.getTimestamp()).build();
    }
}
