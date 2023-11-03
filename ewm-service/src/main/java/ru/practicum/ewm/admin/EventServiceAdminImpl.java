package ru.practicum.ewm.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFindParameters;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.storage.EventRepository;
import ru.practicum.ewm.event.storage.EventSpecification;
import ru.practicum.ewm.exception.ItemDoesNotExistException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceAdminImpl implements EventServiceAdmin {

    private final EventRepository eventRepository;
    private final EventSpecification eventSpecification;

    private final EventMapper eventMapper;

    public List<EventFullDto> getEvents(EventFindParameters parameters, Pageable pageable) {
        log.info("Request for list Events according paramrters {}", parameters);
        return eventRepository.findAll(eventSpecification.getEventsByParameters(parameters), pageable)
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId) {
        existEventById(eventId);
        return eventMapper.toFullDto(eventRepository.findById(eventId).get());
    }

    private void existEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ItemDoesNotExistException("Event with id=" + eventId + " does not exists.");
        }
    }
}