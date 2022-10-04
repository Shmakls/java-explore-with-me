package ru.andrianov.emw.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.compilations.service.PublicCompilationService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilationsByPages(
            @RequestParam(required = false, defaultValue = "true") boolean pinned,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Public)CompilationsController.getCompilationsByPages: received a request to get compilations by" +
                "pages, pinned={}", pinned);

        return publicCompilationService.getCompilationsByPages(pinned, from, size);

    }

    @GetMapping("/{compilationId}")
    private CompilationDto getCompilationById(@PathVariable Long compilationId) {

        log.info("(Public)CompilationsController.getCompilationById: received a request to get compilation by id={}", compilationId);

        return publicCompilationService.getCompilationById(compilationId);

    }

}
