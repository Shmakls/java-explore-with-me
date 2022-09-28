package ru.andrianov.emw.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.stat.dto.EndpointHitsToDistinctIpDto;
import ru.andrianov.emw.stat.mapper.EndpointHitMapper;
import ru.andrianov.emw.stat.model.EndpointHit;
import ru.andrianov.emw.stat.model.EndpointStat;
import ru.andrianov.emw.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHit saveStat(EndpointHit endpointHit) {
        log.info("StatService.saveStat: send a request to DB to save endpointHit");
        return statRepository.save(endpointHit);
    }

    @Override
    public List<EndpointStat> getStatByParams(String start, String end, List<String> uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        List<EndpointHit> hits = statRepository.findEndpointHitsByUriInAndTimestampBetween(uris, startTime, endTime);

        List<EndpointStat> endpointStats = new ArrayList<>();

        if (unique) {

            for (String uri : uris) {
                List<EndpointHitsToDistinctIpDto> hitsByUriDistinct = hits.stream()
                        .filter(x -> x.getUri().equals(uri))
                        .map(EndpointHitMapper::toEndpointHitsToDistinctIpDto)
                        .distinct()
                        .collect(Collectors.toList());

                if (hitsByUriDistinct.size() > 0) {
                    endpointStats.add(new EndpointStat(hitsByUriDistinct.get(0).getApp(),
                            hitsByUriDistinct.get(0).getUri(),
                            (long) hitsByUriDistinct.size()));
                }
            }

        } else {

            for (String uri : uris) {
                List<EndpointHitsToDistinctIpDto> hitsByUri = hits.stream()
                        .filter(x -> x.getUri().equals(uri))
                        .map(EndpointHitMapper::toEndpointHitsToDistinctIpDto)
                        .collect(Collectors.toList());

                if (hitsByUri.size() > 0) {

                    endpointStats.add(new EndpointStat(hitsByUri.get(0).getApp(),
                            hitsByUri.get(0).getUri(),
                            (long) hitsByUri.size()));
                }


            }
        }

        return endpointStats;

    }
}
