package ru.andrianov.emw.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.users.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
