package ru.practicum.ewm.users;

import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User mapUserDtoToUser(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) user.setUserId(user.getUserId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public static List<UserDto> mapUserListToDto(List<User> users) {
        return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }
}
