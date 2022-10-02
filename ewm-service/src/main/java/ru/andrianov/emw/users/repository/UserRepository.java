package ru.andrianov.emw.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> getUsersByIdIn(List<Long> ids, Pageable pageable);

}
