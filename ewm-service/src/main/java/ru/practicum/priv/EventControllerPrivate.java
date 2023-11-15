package ru.practicum.priv;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.event.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventControllerPrivate {

    private static final String EVENT_ID_FIELD_NAME = "id";

    private final EventService eventService;

    public EventControllerPrivate(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(
            @PathVariable(name = "userId") @Positive(message = "Failed to convert value of type java.lang." +
                    "String to required type int; " +
                    "nested exception is java.lang.NumberFormatException: " +
                    "For input string: ad") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, EVENT_ID_FIELD_NAME));
        return eventService.getEvents(userId, pageable);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable(name = "userId") @Positive Long userId,
                                 @RequestBody @Valid final NewEventDto event) {
        return eventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable(name = "userId") @Positive Long userId,
                                 @PathVariable(name = "eventId") @Positive Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "userId") @Positive Long userId,
                                    @PathVariable(name = "eventId") @Positive Long eventId,
                                    @RequestBody @Valid final UpdateEventUserRequest event) {
        return eventService.updateEvent(userId, eventId, event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipation(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId) {
        return eventService.getRequestsForParticipation(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId,
            @RequestBody @Valid final EventRequestStatusUpdateRequest request) {
        return eventService.updateRequestStatus(userId, eventId, request);
    }
}