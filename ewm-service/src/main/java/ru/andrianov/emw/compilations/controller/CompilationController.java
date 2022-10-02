package ru.andrianov.emw.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.business.service.AdminService;
import ru.andrianov.emw.business.service.PublicApiService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final AdminService adminService;

    private final PublicApiService publicApiService;

    @PostMapping("/admin/compilations")
    CompilationDto addNewCompilation(@RequestBody @Valid CompilationToCreateDto compilationToCreateDto) {

        log.info("(Admin)CompilationController.addNewCompilation: received a request to add new compilation");

        return adminService.addNewCompilationByAdmin(compilationToCreateDto);

    }

    @DeleteMapping("/admin/compilations/{compId}")
    void deleteCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.deleteCompilationById: received a request to delete compilation");

        adminService.deleteCompilationByAdminById(compId);

    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    void deleteEventFromCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.deleteEventFromCompilationById: received a request to delete event " +
                "from compilation by admin");

        adminService.deleteEventFromCompilationByIdBYAdmin(compId, eventId);

    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    void addEventToCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.addEventToCompilationById: received a request to add event " +
                "to compilation by admin");

        adminService.addEventToCompilationByIdByAdmin(compId, eventId);

    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    void pinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.pinCompilationById: received a request to pin compilation by admin");

        adminService.pinOrUnpinCompilationByIdByAdmin(compId, true);

    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    void unpinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.unpinCompilationById: received a request to unpin compilation by admin");

        adminService.pinOrUnpinCompilationByIdByAdmin(compId, false);

    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilationsByPages(
            @RequestParam(required = false, defaultValue = "true") boolean pinned,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("(Public)CompilationsController.getCompilationsByPages: received a request to get compilations by" +
                "pages, pinned={}", pinned);

        return publicApiService.getCompilationsByPages(pinned, from, size);

    }

    @GetMapping("/compilations/{compilationId}")
    private CompilationDto getCompilationById(@PathVariable Long compilationId) {

        log.info("(Public)CompilationsController.getCompilationById: received a request to get compilation by id={}", compilationId);

        return publicApiService.getCompilationById(compilationId);

    }

}
