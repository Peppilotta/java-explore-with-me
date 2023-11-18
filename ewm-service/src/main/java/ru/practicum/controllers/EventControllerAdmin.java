package ru.practicum.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.AdminEventsFindParameters;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.services.interfaces.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin/events")
@Validated
public class EventControllerAdmin {

    private static final String EVENT_ID_FIELD_NAME = "id";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService service;

    public EventControllerAdmin(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        AdminEventsFindParameters eventsFindParameters = AdminEventsFindParameters.builder()
                .users(Objects.isNull(users) ? new ArrayList<>() : users)
                .states(Objects.isNull(states) ? new ArrayList<>() : states)
                .categories(Objects.isNull(categories) ? new ArrayList<>() : categories)
                .rangeStart(Objects.isNull(rangeStart) ? LocalDateTime.now() : LocalDateTime.parse(rangeStart, formatter))
                .rangeEnd(Objects.isNull(rangeEnd) ? null : LocalDateTime.parse(rangeEnd, formatter))
                .build();
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, EVENT_ID_FIELD_NAME));
        return service.getEventsByAdmin(eventsFindParameters, pageable);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") @Positive Long eventId) {
        return service.getEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable("eventId") @Positive Long eventId,
                                   @RequestBody @Valid final UpdateEventAdminRequest event) {
        return service.patchEventByAdmin(eventId, event);
    }
}