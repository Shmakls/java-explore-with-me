package ru.andrianov.emw.users.service;

import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.model.User;

import java.util.List;

public interface UserService {

    UserDto addNewUser(User user);

    List<UserDto> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size);

    UserDto getUserById(Long userId);

    void deleteUserById(Long userId);

    boolean existById(Long userId);

    String getUserNameById(Long userId);

}
