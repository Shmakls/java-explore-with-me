package ru.andrianov.emw.users.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.dto.UserInitiatorDto;
import ru.andrianov.emw.users.model.User;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setId(user.getId());

        return userDto;
    }

    public static User fromDto(UserDto userDto) {

        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());

        Optional.ofNullable(userDto.getId()).ifPresent(user::setId);

        return user;

    }

    public static UserInitiatorDto toUserInitiatorDto(User user) {

        UserInitiatorDto userInitiatorDto = new UserInitiatorDto();

        userInitiatorDto.setId(user.getId());
        userInitiatorDto.setName(user.getName());

        return userInitiatorDto;

    }

}
