package ru.andrianov.emw.business.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.andrianov.emw.business.service.AdminService;
import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.events.dto.EventToCompilationDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final AdminService adminService;

    @PostMapping
    List<EventToCompilationDto> addNewCompilation(@RequestBody @Valid CompilationDto compilationDto) {

        log.info("(Admin)CompilationController.addNewCompilation: received a request to add new compilation");

        return adminService.addNewCompilationByAdmin(compilationDto);

    }

    @DeleteMapping("/{compId}")
    void deleteCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.deleteCompilationById: received a request to delete compilation");

        adminService.deleteCompilationByAdminById(compId);

    }

    @DeleteMapping("/{compId}/events/{eventId}")
    void deleteEventFromCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.deleteEventFromCompilationById: received a request to delete event " +
                "from compilation by admin");

        adminService.deleteEventFromCompilationByIdBYAdmin(compId, eventId);

    }

    @PatchMapping("/{compId}/events/{eventId}")
    void addEventToCompilationById(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("(Admin)CompilationController.addEventToCompilationById: received a request to add event " +
                "to compilation by admin");

        adminService.addEventToCompilationByIdByAdmin(compId, eventId);

    }

    @PatchMapping("/{compId}/pin")
    void pinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.pinCompilationById: received a request to pin compilation by admin");

        adminService.pinOrUnpinCompilationByIdByAdmin(compId, true);

    }

    @DeleteMapping("/{compId}/pin")
    void unpinCompilationById(@PathVariable Long compId) {

        log.info("(Admin)CompilationController.unpinCompilationById: received a request to unpin compilation by admin");

        adminService.pinOrUnpinCompilationByIdByAdmin(compId, false);

    }

}
