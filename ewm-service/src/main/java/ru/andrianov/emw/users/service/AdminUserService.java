package ru.andrianov.emw.users.service;

import ru.andrianov.emw.users.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto addNewUser(UserDto userDto);

    List<UserDto> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size);

    UserDto getUserById(Long userId);

    void deleteUserById(Long userId);
}
