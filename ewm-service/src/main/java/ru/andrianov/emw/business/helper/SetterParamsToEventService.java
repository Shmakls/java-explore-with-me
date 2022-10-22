package ru.andrianov.emw.business.helper;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrianov.emw.events.client.EventClient;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.model.EndpointStat;


import java.time.LocalDateTime;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SetterParamsToEventService {

    private final EventClient eventClient;

    public EventToGetDto setCategoryNameAndInitiatorName(EventToGetDto eventToGetDto) {

        eventToGetDto.setViews(getViewsFromStatServiceToEventsDto(eventToGetDto.getId()));

        return eventToGetDto;

    }

    public EventToCompilationDto setCategoryNameAndInitiatorName(EventToCompilationDto eventToCompilationDto) {

        eventToCompilationDto.setViews(getViewsFromStatServiceToEventsDto(eventToCompilationDto.getId()));

        return eventToCompilationDto;
    }

    private Long getViewsFromStatServiceToEventsDto(Long eventId) {

        String apiPrefix = "/events/";

        String start = DateTimeStringConverter.toFormattedString(LocalDateTime.now().minusYears(20));
        String end = DateTimeStringConverter.toFormattedString(LocalDateTime.now().plusYears(1));

        List<String> uris = List.of(apiPrefix + eventId);

        ResponseEntity<List<EndpointStat>> response = eventClient.getStat(start, end, uris);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Ошибка на сервисе статистики");
        }

        List<EndpointStat> stats = response.getBody();

        if (stats == null) {
            return 0L;
        } else if (stats.size() > 0) {
            return stats.get(0).getHits();
        } else {
            return 0L;
        }

    }

}
