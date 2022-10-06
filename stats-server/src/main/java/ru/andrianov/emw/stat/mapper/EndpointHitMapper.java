package ru.andrianov.emw.stat.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.andrianov.emw.stat.model.EndpointHit;
import ru.andrianov.emw.stat.model.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {

        EndpointHit endpointHit = new EndpointHit();

        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());

        endpointHit.setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), FORMATTER));

        return endpointHit;

    }




}
