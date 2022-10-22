package ru.andrianov.emw.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.users.dto.UserDto;
import ru.andrianov.emw.users.exceptions.UserNotFoundException;
import ru.andrianov.emw.users.mapper.UserMapper;
import ru.andrianov.emw.users.model.User;
import ru.andrianov.emw.users.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addNewUser(UserDto userDto) {

        log.info("UserService.addNewUser: send a request to DB to create new user with email={}", userDto.getEmail());

        return userRepository.save(UserMapper.fromDto(userDto));
    }

    @Override
    public List<User> getUsersByIdInByPages(List<Long> ids, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());

        log.info("UserService.getAllUsers: send a request to DB to get user by ids={} by pages", ids);

        return userRepository.getUsersByIdIn(ids, pageable).getContent();

    }

    @Override
    public User getUserById(Long userId) {

        log.info("UserService.getUserById: send a request to DB to get user with id={}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user is not exist"));
    }

    @Override
    public void deleteUserById(Long userId) {

        if (!existById(userId)) {
            log.error("UserService.deleteUserById: user with id={} not exist", userId);
            throw new UserNotFoundException("user not exist");
        }

        log.info("UserService.deleteUserById: send a request to DB to delete user with id={}", userId);
        userRepository.deleteById(userId);

    }

    @Override
    public String getUserNameById(Long userId) {

        return userRepository.findUserNameById(userId)
                .orElseThrow(() -> new UserNotFoundException("user is not exist"));

    }

    @Override
    public boolean existById(Long userId) {
        return userRepository.existsById(userId);
    }
}
