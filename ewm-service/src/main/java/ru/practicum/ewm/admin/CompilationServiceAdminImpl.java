package ru.practicum.ewm.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.NewEventCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.EventCompilation;
import ru.practicum.ewm.compilation.storage.CompilationRepository;
import ru.practicum.ewm.compilation.storage.EventCompilationRepository;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.storage.EventRepository;
import ru.practicum.ewm.exception.ItemDoesNotExistException;
import ru.practicum.ewm.request.dto.UpdateCompilationRequest;

import java.util.ArrayList;
import java.util.List;
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

    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info("Request for creating Compilation = {}", compilationDto);

        Compilation compilation = compilationMapper.toCompilation(compilationDto);
        Compilation createdCompilation = compilationRepository.save(compilation);
        return compilationMapper
                .toCompilationDto(createdCompilation, addEventToCompilation(
                        compilationDto.getEvents(), createdCompilation.getId()));
    }

    public CompilationDto deleteCompilation(Long compId) {
        log.info("Request for deleting Compilation with id = {}", compId);
        checkCompilationExists(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        List<Long> eventIds = eventCompilationRepository.findByCompilationId(compId);
        List<EventShortDto> events = eventRepository.findAllByIds(eventIds)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
        compilationRepository.deleteById(compId);
        eventCompilationRepository.deleteEvents(compId, eventIds);
        return compilationMapper.toCompilationDto(compilation, events);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationNew) {
        log.info("Request for updating Compilation with id = {} and Updates = {}", compId, compilationNew);
        checkCompilationExists(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setTitle(compilationNew.getTitle());
        compilation.setPinned(compilationNew.getPinned());
        Compilation createdCompilation = compilationRepository.save(compilation);
        List<Long> eventIds = eventCompilationRepository.findByCompilationId(compId);
        List<Long> newEventIds = compilationNew.getEvents();
        newEventIds.removeAll(eventIds);
        return compilationMapper.toCompilationDto(createdCompilation, addEventToCompilation(newEventIds, compId));
    }

    private List<EventShortDto> addEventToCompilation(List<Long> eventIds, Long id) {
        if (eventIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<EventCompilation> eventCompilations = eventIds.stream()
                    .map(e -> compilationMapper.toEventCompilation(new NewEventCompilationDto(id, e)))
                    .collect(Collectors.toList());
            eventCompilationRepository.saveAll(eventCompilations);
            return eventRepository.findAllByIds(eventIds)
                    .stream()
                    .map(eventMapper::toShortDto)
                    .collect(Collectors.toList());
        }
    }

    private void checkCompilationExists(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            String message = "Compilation with id=" + compId + " not exists";
/*
            ApiError apiError = ApiError.builder()
                    .message(message)
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
*/
            throw new ItemDoesNotExistException(message);
        }
    }
}