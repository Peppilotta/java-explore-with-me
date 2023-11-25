package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.CompilationWithoutEvent;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.NewEventCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.EventCompilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.compilation.storage.EventCompilationRepository;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.services.interfaces.CompilationServiceAdmin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {

    private final CompilationMapper compilationMapper;

    private final CompilationRepository compilationRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info("Request for creating Compilation = {}", compilationDto);
        checkName(compilationDto.getTitle());
        Compilation compilation = compilationMapper.toCompilation(compilationDto);
        Compilation createdCompilation = compilationRepository.save(compilation);
        List<Long> eventIds = compilationDto.getEvents();
        List<EventShortDto> events = new ArrayList<>();
        if (!Objects.isNull(eventIds) && !eventIds.isEmpty()) {
            events.addAll(addEventToCompilation(eventIds, createdCompilation.getId()));
        }
        return compilationMapper.toCompilationDto(createdCompilation, events);
    }

    @Override
    @Transactional
    public CompilationWithoutEvent deleteCompilation(Long compId) {
        log.info("Request for deleting Compilation with id = {}", compId);
        checkCompilationExists(compId);
        Compilation found = compilationRepository.findById(compId).get();
        CompilationWithoutEvent deleted = new CompilationWithoutEvent(found.getId(), found.getPinned(), found.getTitle());
        List<Long> eventIds = eventCompilationRepository.findByCompilationId(compId);
        eventCompilationRepository.deleteEvents(compId, eventIds);
        compilationRepository.deleteById(compId);
        return deleted;
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationUpdate) {
        log.info("Request for updating Compilation with id = {} and Updates = {}", compId, compilationUpdate);
        checkCompilationExists(compId);
        Compilation compilation = compilationRepository.findById(compId).orElseGet(Compilation::new);

        if (!Objects.isNull(compilationUpdate.getTitle())) {
            checkName(compilationUpdate.getTitle());
            compilation.setTitle(compilationUpdate.getTitle());
        }
        if (!Objects.isNull(compilationUpdate.getPinned())) {
            compilation.setPinned(compilationUpdate.getPinned());
        }
        List<Long> eventIds = eventCompilationRepository.findByCompilationId(compId);
        List<EventShortDto> newEvents = new ArrayList<>();
        if (!Objects.isNull(compilationUpdate.getEvents())) {
            List<Long> updatedEventIds = compilationUpdate.getEvents();
            if (!eventIds.isEmpty()) {
                updatedEventIds.removeAll(eventIds);
            }
            List<Long> existedNewEventIds = updatedEventIds.stream()
                    .filter(eventRepository::existsById)
                    .collect(Collectors.toList());

            newEvents.addAll(addEventToCompilation(existedNewEventIds, compId));
        }
        newEvents.addAll(eventMapper.toShortDtos(eventRepository.findAllByIds(eventIds)));
        return compilationMapper.toCompilationDto(
                compilationRepository.save(compilation), newEvents);
    }

    private List<EventShortDto> addEventToCompilation(List<Long> eventIds, Long id) {
        if (eventIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<EventCompilation> eventCompilations = eventIds.stream()
                .map(e -> compilationMapper.toEventCompilation(new NewEventCompilationDto(id, e)))
                .collect(Collectors.toList());
        eventCompilationRepository.saveAll(eventCompilations);
        return eventMapper.toShortDtos(eventRepository.findAllByIds(eventIds));
    }

    private void checkName(String title) {
        if (compilationRepository.existsByTitle(title)) {
            ApiError apiError = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
                    "Conflict in Unique name.",
                    "Compilation with title=" + title + " exists",
                    LocalDateTime.now());
            throw new ConflictException(apiError);
        }
    }

    private void checkCompilationExists(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            ApiError apiError = new ApiError(ErrorStatus.E_404_NOT_FOUND.getValue(),
                    "The required object was not found.",
                    "Compilation with id=" + compId + " not exists",
                    LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }
}