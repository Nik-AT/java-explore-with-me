package ru.practicum.ewm.users.admin;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.BadRequestParametersException;
import ru.practicum.ewm.exceptions.ModelAlreadyExistsException;
import ru.practicum.ewm.exceptions.ModelNotFoundException;
import ru.practicum.ewm.users.UserRepository;
import ru.practicum.ewm.users.model.User;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User postNewUser(User user) {
        if (user.getUserId() != null) throw new BadRequestParametersException("UserId must be null!");
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ModelAlreadyExistsException("This email is used!");
        }
    }

    public void deleteUserById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ModelNotFoundException(e.getMessage());
        }

    }

    public List<User> getAllByIdsWithPagination(Long[] ids, int from, int size) {
        return repository.findAllByIdsWithPagination(Arrays.asList(ids), from, size);
    }
}
