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
import ru.practicum.request.dto.UpdateEventUserRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventControllerPrivate {

    private static final String EVENT_ID_FIELD_NAME = "id";

    private final UserEventService userEventService;

    public EventControllerPrivate(UserEventService userEventService) {
        this.userEventService = userEventService;
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
        return userEventService.getEvents(userId, pageable);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable(name = "userId") @Positive Long userId,
                                 @RequestBody @Validated final NewEventDto event) {
        return userEventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable(name = "userId") @Positive Long userId,
                                 @PathVariable(name = "eventId") @Positive Long eventId) {
        return userEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "userId") @Positive Long userId,
                                    @PathVariable(name = "eventId") @Positive Long eventId,
                                    @RequestBody @Validated final UpdateEventUserRequest event) {
        return userEventService.updateEvent(userId, eventId, event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipation(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId) {
        return userEventService.getRequestsForParticipation(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId,
            @RequestBody @Validated final EventRequestStatusUpdateRequest request) {
        return userEventService.updateRequestStatus(userId, eventId, request);
    }
}