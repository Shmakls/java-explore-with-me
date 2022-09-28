package ru.andrianov.emw.stat.service;

import ru.andrianov.emw.stat.model.EndpointHit;
import ru.andrianov.emw.stat.model.EndpointStat;

import java.util.List;

public interface StatService {
    EndpointHit saveStat(EndpointHit endpointHit);

    List<EndpointStat> getStatByParams(String start, String end, List<String> uris, Boolean unique);
}
