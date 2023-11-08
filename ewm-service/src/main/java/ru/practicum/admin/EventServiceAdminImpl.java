package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventsFindParameters;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.EventSpecification;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceAdminImpl implements EventServiceAdmin {

    private final EventRepository eventRepository;
    private final EventSpecification eventSpecification;

    private final EventMapper eventMapper;

    public List<EventFullDto> getEvents(EventsFindParameters parameters, Pageable pageable) {
        log.info("Request for list Events according parameters {}", parameters);
        return eventRepository.findAll(eventSpecification.getEventsByParameters(parameters), pageable)
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId) {
        existEventById(eventId);
        return eventMapper.toFullDto(eventRepository.findById(eventId).orElseGet(Event::new));
    }

    private void existEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            ApiError apiError = ApiError.builder()
                    .message("Event with id=" + eventId + " does not exists.")
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();

            throw new NotFoundException(apiError);
        }
    }
}