package ru.andrianov.emw.stat.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class EndpointHitsToDistinctIpDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timeStamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHitsToDistinctIpDto that = (EndpointHitsToDistinctIpDto) o;
        return ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
