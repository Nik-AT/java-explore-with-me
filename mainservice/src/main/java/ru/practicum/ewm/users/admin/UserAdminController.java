package ru.practicum.ewm.users.admin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.users.UserMapper;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    public UserDto postUser(@RequestBody UserDto userDto) {
        if (userDto.getEmail().isEmpty() || userDto.getName().isEmpty()) throw new RuntimeException("Wrong Dto Body!");
        User user = UserMapper.mapUserDtoToUser(userDto);
        return UserMapper.mapToUserDto(userService.postNewUser(user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllWithPaginationByIdList(@RequestParam Long[] ids,
                                                      @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        return UserMapper.mapUserListToDto(userService.getAllByIdsWithPagination(ids, from, size));
    }
}
