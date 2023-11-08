package ru.practicum.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.compilation.storage.EventCompilationRepository;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final EventCompilationRepository eventCompilationRepository;

    private final CompilationMapper compilationMapper;

    public List<CompilationDto> getCompilations(String pined, Pageable pageable) {
        Page<Compilation> compilations = compilationRepository.getCompilationsByPinned(pined, pageable);
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation comp : compilations) {
            compilationDtos.add(compilationMapper.toCompilationDto(comp, getEvents(comp.getId())));
        }
        return compilationDtos;
    }

    public CompilationDto getCompilation(Long compId) {
        checkCompilationExists(compId);
        return compilationMapper.toCompilationDto(
                compilationRepository.findById(compId).orElseGet(Compilation::new), getEvents(compId));
    }

    private List<EventShortDto> getEvents(Long compId) {
        List<Long> eventIds = eventCompilationRepository.findByCompilationId(compId);
        List<Event> events = eventRepository.findAllByIds(eventIds);
        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    private void checkCompilationExists(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            String message = "Compilation with id=" + compId + " not exists";
            ApiError apiError = ApiError.builder()
                    .message(message)
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiError);
        }
    }
}