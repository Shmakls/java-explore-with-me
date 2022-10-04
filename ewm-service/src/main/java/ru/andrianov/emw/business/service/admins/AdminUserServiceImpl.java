package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.users.service.AdminUserService;
import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserService userService;

    @Override
    public UserDto addNewUser(UserDto userDto) {
        return userService.addNewUser(userDto);
    }

    @Override
    public List<UserDto> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size) {
        return userService.getUsersByIdInByPages(ids, from, size);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }
}
