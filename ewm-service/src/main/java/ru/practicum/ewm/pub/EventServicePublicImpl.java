package ru.practicum.ewm.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.error.ErrorStatus;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.PublicEventFindParameters;
import ru.practicum.ewm.event.dto.PublicEventsFindParameters;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.storage.EventRepository;
import ru.practicum.ewm.event.storage.EventSpecification;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.storage.RequestRepository;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {

    private final EventRepository eventRepository;
    private final EventSpecification eventSpecification;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final StatsClient client;

    public List<EventShortDto> getEvents(PublicEventsFindParameters parameters, Pageable pageable) {
        saveStatistic(parameters.getPublicIp(), parameters.getUri());
        int start = (int) pageable.getOffset();
        int end;
        List<Event> eventsPageable = new ArrayList<>();
        List<Event> events = eventRepository.findAll(eventSpecification.getEventsByParametersPublic(parameters));
        if (Boolean.TRUE.equals(parameters.getOnlyAvailable())) {
            List<Event> filtered = events.stream()
                    .filter(e -> (e.getParticipantLimit() > 0)
                            && (e.getEventRequests().size()
                            < requestRepository.getConfirmedRequestsForEventWithId(e.getId(), RequestStatus.CONFIRMED)))
                    .collect(Collectors.toList());
            if (filtered.isEmpty()) {
                return new ArrayList<>();
            } else {
                end = Math.min((start + pageable.getPageSize()), filtered.size());
                eventsPageable.addAll(filtered.subList(start, end));
            }
        } else {
            end = Math.min((start + pageable.getPageSize()), events.size());
            eventsPageable.addAll(events.subList(start, end));
        }

        if (eventsPageable.isEmpty()) {
            return new ArrayList<>();
        } else {
            eventRepository.updateViewsByEvents(eventsPageable);
            return eventMapper.toShortDtos(eventRepository.findAllByEvents(eventsPageable));
        }
    }

    public EventFullDto getEvent(PublicEventFindParameters parameters) {
        Long id = parameters.getEventId();
        checkEventExists(id);
        saveStatistic(parameters.getPublicIp(), parameters.getUri());
        eventRepository.updateViewsById(id);
        return eventMapper.toFullDto(eventRepository.findById(id).orElseGet(Event::new));
    }

    private void checkEventExists(Long id) {
        if (!eventRepository.existsById(id)) {
            ApiError apiError = ApiError.builder()
                    .message("Event with id=" + id + " was not found")
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiError);
        }
    }

    private void saveStatistic(String publicIp, String uri) {
        EndpointHitDto hit = EndpointHitDto.builder()
                .app("ewm_service")
                .timestamp(LocalDateTime.now())
                .uri(uri)
                .ip(publicIp)
                .build();
        ResponseEntity<Object> response = client.postHit(hit);
/*
        EndpointHitDto hitCreated = objectMapper.readValue((JsonParser) response.getBody(), EndpointHitDto.class);
        log.debug("<public> save event view to statistic: {}", hitCreated);
*/
    }
}