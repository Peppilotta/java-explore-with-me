package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicEventFindParameters;
import ru.practicum.event.dto.PublicEventsFindParameters;
import ru.practicum.event.dto.SortEvent;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.EventSpecification;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.services.interfaces.EventServicePublic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {

    private final EventRepository eventRepository;
    private final EventSpecification eventSpecification;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;

    private final EventMapper eventMapper;

    private final StatsClient client;

    @Override
    public List<EventShortDto> getEvents(PublicEventsFindParameters parameters, Pageable pageable) {
        log.info("Get events with parameters Public");
        checkCategoriesInParameters(parameters.getCategories());
        List<Event> events = eventRepository
                .findAll(eventSpecification.getEventsByParametersPublic(parameters), pageable).getContent();
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        List<Event> filtered = new ArrayList<>();
        if (Boolean.TRUE.equals(parameters.getOnlyAvailable())) {
            filtered.addAll(events.stream()
                    .filter(e -> ((e.getParticipantLimit() > 0)
                            && (e.getEventRequests().size()
                            < requestRepository.getAllByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED))
                            || e.getParticipantLimit() == 0))
                    .collect(Collectors.toList()));
            if (filtered.isEmpty()) {
                return new ArrayList<>();
            }
        } else {
            filtered.addAll(events);
        }

        Long visitsWithIp = saveStatistic(parameters.getPublicIp(), parameters.getUri());
        if (visitsWithIp == 0) {
            eventRepository.updateViewsByEvents(filtered);
        }
        log.info("views for events updated");
        if (!Objects.isNull(parameters.getSort()) && SortEvent.existsByName(parameters.getSort())) {
            SortEvent sort = SortEvent.valueOf(parameters.getSort());
            if (Objects.equals(sort, SortEvent.EVENT_DATE)) {
                filtered.sort(Comparator.comparing(Event::getEventDate));
            }
            if (Objects.equals(sort, SortEvent.VIEWS)) {
                filtered.sort(Comparator.comparing(Event::getViews));
            }
        }

        return eventMapper.toShortDtos(filtered);
    }

    @Override
    public EventFullDto getEvent(PublicEventFindParameters parameters) {
        log.info("Get event with parameters Public");
        Long id = parameters.getEventId();
        checkEventExists(id);
        Event event = eventRepository.findById(id).orElseGet(Event::new);
        checkEventPublished(event.getState());
        Long visitsWithIp = saveStatistic(parameters.getPublicIp(), parameters.getUri());
        if (visitsWithIp == 0) {
            eventRepository.updateViewsById(id);
            log.info("views for event with id={} updated", id);
        }

        return eventMapper.toFullDto(event);
    }

    private void checkCategoriesInParameters(List<Long> categories) {
        if (!Objects.isNull(categories) && !categories.isEmpty()) {
            ApiError apiError = ApiError.builder()
                    .message("Bad categories parameter")
                    .reason("Bad request parameters")
                    .status(ErrorStatus.E_400_BAD_REQUEST.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();

            categories.forEach(u ->
                    categoryRepository.findById(u).orElseThrow(() ->
                            new BadRequestException(apiError)));
        }
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

    private void checkEventPublished(EventLifeState state) {
        if (!Objects.equals(state, EventLifeState.PUBLISHED)) {
            ApiError apiError = ApiError.builder()
                    .message("Event not Published")
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiError);
        }
    }

    private Long saveStatistic(String publicIp, String uri) {
        String app = "ewm_service";
        Long visitWithIp = client.getVisitorsIp(app, uri, publicIp);
        log.info("ip={}, count={}", publicIp, visitWithIp);
        EndpointHitDto hit = EndpointHitDto.builder()
                .app(app)
                .timestamp(LocalDateTime.now())
                .uri(uri)
                .ip(publicIp)
                .build();
        EndpointHitDto response = client.postHit(hit);
        log.info("Statistic saved. Hit = {}", response);
        return visitWithIp;
    }
}