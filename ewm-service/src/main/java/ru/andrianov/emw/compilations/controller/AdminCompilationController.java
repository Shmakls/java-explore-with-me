package ru.andrianov.emw.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.compilations.service.AdminCompilationService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.dto.CompilationToCreateDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    CompilationDto addNewCompilation(@RequestBody @Valid CompilationToCreateDto compilationToCreateDto) {

        log.info("(Admin)CompilationController.addNewCompilation: received a request to add new compilation");

        return adminCompilationService.addNewCompilationByAdmin(compilationToCreateDto);

    }

    @DeleteMapping("/{compId}")
    void deleteCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.deleteCompilationById: received a request to delete compilation");

        adminCompilationService.deleteCompilationByAdminById(compId);

    }

    @DeleteMapping("/{compId}/events/{eventId}")
    void deleteEventFromCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.deleteEventFromCompilationById: received a request to delete event " +
                "from compilation by admin");

        adminCompilationService.deleteEventFromCompilationByIdByAdmin(compId, eventId);

    }

    @PatchMapping("/{compId}/events/{eventId}")
    void addEventToCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.addEventToCompilationById: received a request to add event " +
                "to compilation by admin");

        adminCompilationService.addEventToCompilationByIdByAdmin(compId, eventId);

    }

    @PatchMapping("/{compId}/pin")
    void pinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.pinCompilationById: received a request to pin compilation by admin");

        adminCompilationService.pinOrUnpinCompilationByIdByAdmin(compId, true);

    }

    @DeleteMapping("/{compId}/pin")
    void unpinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.unpinCompilationById: received a request to unpin compilation by admin");

        adminCompilationService.pinOrUnpinCompilationByIdByAdmin(compId, false);

    }

}
