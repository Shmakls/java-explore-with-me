package ru.andrianov.emw.stat.mapper;

import ru.andrianov.emw.stat.dto.EndpointHitsToDistinctIpDto;
import ru.andrianov.emw.stat.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHitsToDistinctIpDto toEndpointHitsToDistinctIpDto(EndpointHit endpointHit) {

        EndpointHitsToDistinctIpDto endpointHitsToDistinctIpDto = new EndpointHitsToDistinctIpDto();

        endpointHitsToDistinctIpDto.setId(endpointHit.getId());
        endpointHitsToDistinctIpDto.setApp(endpointHit.getApp());
        endpointHitsToDistinctIpDto.setUri(endpointHit.getUri());
        endpointHitsToDistinctIpDto.setIp(endpointHit.getIp());
        endpointHitsToDistinctIpDto.setTimeStamp(endpointHit.getTimestamp());

        return endpointHitsToDistinctIpDto;

    }

}
