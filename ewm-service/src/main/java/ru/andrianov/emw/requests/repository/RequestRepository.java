package ru.andrianov.emw.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.requests.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
