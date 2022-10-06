package ru.andrianov.emw.users.service;

import ru.andrianov.emw.users.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addNewUser(UserDto userDto);

    List<UserDto> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size);

    UserDto getUserById(Long userId);

    void deleteUserById(Long userId);

    boolean existById(Long userId);

    String getUserNameById(Long userId);

}
