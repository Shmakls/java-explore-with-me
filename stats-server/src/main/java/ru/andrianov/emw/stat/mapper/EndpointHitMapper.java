package ru.andrianov.emw.stat.mapper;

import ru.andrianov.emw.stat.model.EndpointHit;
import ru.andrianov.emw.stat.model.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {

        EndpointHit endpointHit = new EndpointHit();

        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        endpointHit.setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter));

        return endpointHit;

    }




}
