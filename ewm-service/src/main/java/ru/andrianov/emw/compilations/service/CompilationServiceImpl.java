package ru.andrianov.emw.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.compilations.exceptions.CompilationNotFoundException;
import ru.andrianov.emw.compilations.model.Compilation;
import ru.andrianov.emw.compilations.model.CompilationForList;
import ru.andrianov.emw.compilations.repository.CompilationListRepository;
import ru.andrianov.emw.compilations.repository.CompilationRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationListRepository compilationListRepository;

    @Override
    public Compilation addNewCompilationByAdmin(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public CompilationForList saveCompilationList(CompilationForList compilationForList) {
        return compilationListRepository.save(compilationForList);
    }

    @Override
    public void deleteCompilationById(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        compilationListRepository.deleteAllByCompilationId(compilationId);
    }

    @Override
    public void deleteEventFromCompilationListById(Long eventId) {
        compilationListRepository.deleteByEventId(eventId);
    }

    @Override
    public Compilation getCompilationById(Long compilationId) {

        if(!existCompilationById(compilationId)) {
            log.error("CompilationService.getCompilationById: compilation with id={} not found", compilationId);
            throw new CompilationNotFoundException("compilation not found");
        }

        return compilationRepository.getReferenceById(compilationId);
    }

    @Override
    public List<CompilationForList> getListOfCompilationsForListByCompilationId(Long compilationId) {

        if(!existCompilationById(compilationId)) {
            log.error("CompilationService.getListOfCompilationsForListByCompilationId: compilation with id={} not found", compilationId);
            throw new CompilationNotFoundException("compilation not found");
        }

        return compilationListRepository.findAllByCompilationId(compilationId);
    }

    @Override
    public Compilation updateCompilationById(Compilation compilation) {

        if(!existCompilationById(compilation.getId())) {
            log.error("CompilationService.getCompilationById: compilation with id={} not found", compilation.getId());
            throw new CompilationNotFoundException("compilation not found");
        }

        return compilationRepository.save(compilation);

    }

    @Override
    public List<Compilation> getAllCompilationsByPinnedByPages(boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return compilationRepository.findCompilationsByPinned(pinned, pageable).getContent();
    }

    @Override
    public boolean existCompilationById(Long compilationId) {
        return compilationRepository.existsById(compilationId);
    }

    @Override
    public boolean existEventInCompilationById(Long compilationId, Long eventId) {
        if(!existCompilationById(compilationId)) {
            log.error("CompilationService.existEventInCompilationById: compilation with id={} not found", compilationId);
            throw new CompilationNotFoundException("compilation not found");
        }

        List<CompilationForList> eventsListByCompilation = compilationListRepository.findAllByCompilationId(compilationId);

        for (CompilationForList compilationForList : eventsListByCompilation) {
            if (compilationForList.getEventId().equals(eventId)) {
                return true;
            }
        }

        return false;

    }
}
