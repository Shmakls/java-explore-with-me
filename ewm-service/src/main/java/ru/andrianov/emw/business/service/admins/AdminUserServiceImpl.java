package ru.andrianov.emw.business.service.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.users.mapper.UserMapper;
import ru.andrianov.emw.users.service.AdminUserService;
import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserService userService;

    @Override
    public UserDto addNewUser(UserDto userDto) {
        return UserMapper.toDto(userService.addNewUser(userDto));
    }

    @Override
    public List<UserDto> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size) {
        return userService.getUsersByIdInByPages(ids, from, size)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toDto(userService.getUserById(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }
}
