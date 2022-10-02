package ru.andrianov.emw.business.service;

import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.dto.EventToUpdateByAdminDto;

import java.util.List;

public interface AdminService {

    List<EventToGetDto> eventSearchByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, Integer from, Integer size);

    EventToGetDto updateEventByAdmin(EventToUpdateByAdminDto eventToUpdateByAdminDto, Long eventId);

    EventToGetDto publishEventByAdmin(Long eventId);

    EventToGetDto rejectEventByAdmin(Long eventId);

    CompilationDto addNewCompilationByAdmin(CompilationToCreateDto compilationToCreateDto);

    void deleteCompilationByAdminById(Long compId);

    void deleteEventFromCompilationByIdBYAdmin(Long compId, Long eventId);

    void addEventToCompilationByIdByAdmin(Long compId, Long eventId);

    void pinOrUnpinCompilationByIdByAdmin(Long compId, boolean pinned);

}
