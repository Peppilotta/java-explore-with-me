package ru.practicum.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicEventFindParameters;
import ru.practicum.event.dto.PublicEventsFindParameters;
import ru.practicum.services.interfaces.EventServicePublic;

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
    @ResponseStatus(HttpStatus.OK)
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

        PublicEventsFindParameters eventFindParameters = new PublicEventsFindParameters();
        eventFindParameters.setText(text);
        eventFindParameters.setCategories(Objects.isNull(categories) ? new ArrayList<>() : categories);
        eventFindParameters.setPaid(paid);
        eventFindParameters.setRangeStart(Objects.isNull(rangeStart)
                ? LocalDateTime.now()
                : LocalDateTime.parse(rangeStart, formatter));
        eventFindParameters.setRangeEnd(Objects.isNull(rangeEnd) ? null : LocalDateTime.parse(rangeEnd, formatter));
        eventFindParameters.setOnlyAvailable(onlyAvailable);
        eventFindParameters.setSort(sort);
        eventFindParameters.setPublicIp(request.getRemoteAddr());
        eventFindParameters.setUri(request.getRequestURI());

        Pageable pageable = PageRequest.of(from / size, size);

        return service.getEvents(eventFindParameters, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable @Positive Long id,
                                 HttpServletRequest request) {
        PublicEventFindParameters parameters = new PublicEventFindParameters();
        parameters.setEventId(id);
        parameters.setPublicIp(request.getRemoteAddr());
        parameters.setUri(request.getRequestURI());
        return service.getEvent(parameters);
    }
}