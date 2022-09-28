package ru.andrianov.emw.users.сontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.model.User;
import ru.andrianov.emw.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@RequestBody @Valid User user) {
        log.info("(Admin)UserController.addNewUser: received request to add new user email={}", user.getEmail());
        return userService.addNewUser(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("(Admin)UserController.getAllUsers: received request to get all users");
        return userService.getAllUsers(from, size);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("(Admin)UserController.getUserById: received request to get user with id={}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("(Admin)UserController.getUserById: received request to delete user with id={}", userId);
        userService.deleteUserById(userId);
    }

}
