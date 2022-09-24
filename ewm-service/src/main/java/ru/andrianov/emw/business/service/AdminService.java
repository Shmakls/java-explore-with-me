package ru.andrianov.emw.business.service;

import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;

import java.util.List;

public interface AdminService {

    List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, Integer from, Integer size);

    EventToGetDto updateEventByAdmin(EventToCreateDto eventToCreateDto, Long eventId);

    EventToGetDto publishEventByAdmin(Long eventId);

    EventToGetDto rejectEventByAdmin(Long eventId);

    List<EventToCompilationDto> addNewCompilationByAdmin(CompilationDto compilationDto);

    void deleteCompilationByAdminById(Long compId);

    void deleteEventFromCompilationByIdBYAdmin(Long compId, Long eventId);

    void addEventToCompilationByIdByAdmin(Long compId, Long eventId);

    void pinOrUnpinCompilationByIdByAdmin(Long compId, boolean pinned);

}
