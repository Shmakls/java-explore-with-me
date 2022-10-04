package ru.andrianov.emw.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.stat.mapper.EndpointHitMapper;
import ru.andrianov.emw.stat.model.EndpointHit;
import ru.andrianov.emw.stat.model.EndpointHitDto;
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
    public EndpointHit saveStat(EndpointHitDto endpointHitDto) {
        log.info("StatService.saveStat: send a request to DB to save endpointHitDto");

        return statRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<EndpointStat> getStatByParams(String start, String end, List<String> uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        List<EndpointStat> endpointStats = new ArrayList<>();

        List<EndpointHit> hits = new ArrayList<>();

        uris = uris.stream()
                .map(x -> x.replace("[", ""))
                .map(x -> x.replace("]", ""))
                .collect(Collectors.toList());

        if (unique) {
            hits = statRepository.findStatByUrisByTimeDistinct(startTime, endTime, uris);
        } else {
            hits = statRepository.getEndpointHitsByUriInAndTimestampBetween(uris, startTime, endTime);
        }

        for (String uri : uris) {
            hits = hits.stream()
                    .filter(x -> x.getUri().equals(uri))
                    .collect(Collectors.toList());

            if (hits.size() > 0) {
                endpointStats.add(new EndpointStat(hits.get(0).getApp(),
                        hits.get(0).getUri(),
                        (long) hits.size()));
            }
        }

        return endpointStats;

    }
}
