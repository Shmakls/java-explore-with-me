package ru.andrianov.emw.business.service;

import ru.andrianov.emw.categories.model.Category;
import ru.andrianov.emw.events.dto.EventToCompilationDto;
import ru.andrianov.emw.events.dto.EventToGetDto;
import ru.andrianov.emw.events.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicApiService {


    List<Category> getAllCategoriesByPages(Integer from, Integer size);

    Category getCategoryById(Long categoryId);

    List<List<EventToCompilationDto>> getCompilationsByPages(boolean pinned, Integer from, Integer size);

    List<EventToCompilationDto> getCompilationById(Long compilationId);

    EventToGetDto getEventForPublicById(Long eventId, HttpServletRequest request);

    List<EventToGetDto> searchEventsByParams(String text, List<Long> categoriesId,
                                             boolean paid, String rangeStart, String rangeEnd,
                                             boolean onlyAvailable, EventSort eventSort,
                                             Integer from, Integer size, HttpServletRequest request);
}