package ru.andrianov.emw.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.users.service.AdminUserService;
import ru.andrianov.emw.users.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public UserDto addNewUser(@RequestBody @Valid UserDto userDto) {
        log.info("(Admin)UserController.addNewUser: received request to add new user email={}", userDto.getEmail());
        return adminUserService.addNewUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsersByIdInByPages(@RequestParam List<Long> ids,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("(Admin)UserController.getUsersByIdInByPages: received request to get all users");
        return adminUserService.getUsersByIdInByPages(ids, from, size);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("(Admin)UserController.getUserById: received request to get user with id={}", userId);
        return adminUserService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("(Admin)UserController.getUserById: received request to delete user with id={}", userId);
        adminUserService.deleteUserById(userId);
    }

}
