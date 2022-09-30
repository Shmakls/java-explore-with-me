package ru.andrianov.emw.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andrianov.emw.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT ip, uri, app " +
                    "FROM endpoint_stat " +
                    "WHERE timestamp between ? and ? " +
                    "      and uri IN ? " +
                    "GROUP BY uri")
    List<EndpointHit> findStatByUrisByTimeDistinct(LocalDateTime startTime, LocalDateTime endTime,
                                                   List<String> uris);

    List<EndpointHit> findEndpointHitsByUriInAndTimestampBetween(List<String> uris, LocalDateTime start,
                                                                 LocalDateTime end);

}
