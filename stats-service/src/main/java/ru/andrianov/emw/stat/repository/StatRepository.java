package ru.andrianov.emw.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findEndpointHitsByUriInAndTimestampBetween(List<String> uris, LocalDateTime startTime,
                                                                 LocalDateTime endTime);

}
