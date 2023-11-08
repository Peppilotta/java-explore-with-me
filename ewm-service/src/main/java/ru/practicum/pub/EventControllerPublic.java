package ru.practicum.pub;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicEventFindParameters;
import ru.practicum.event.dto.PublicEventsFindParameters;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/events")
@Validated
public class EventControllerPublic {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventServicePublic service;

    public EventControllerPublic(EventServicePublic service) {
        this.service = service;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {

        PublicEventsFindParameters eventFindParameters = PublicEventsFindParameters.builder()
                .text(text)
                .categories(Objects.isNull(categories) ? new ArrayList<>() : categories)
                .paid(paid)
                .rangeStart(Objects.isNull(rangeStart) ? LocalDateTime.now() : LocalDateTime.parse(rangeStart, formatter))
                .rangeEnd(Objects.isNull(rangeEnd) ? null : LocalDateTime.parse(rangeEnd, formatter))
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .publicIp(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .build();

        Pageable pageable = PageRequest.of(from / size, size);

        return service.getEvents(eventFindParameters, pageable);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable(name = "id") @Positive Long id,
                                 HttpServletRequest request) {
        PublicEventFindParameters parameters = PublicEventFindParameters.builder()
                .eventId(id)
                .publicIp(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .build();
        return service.getEvent(parameters);
    }
}