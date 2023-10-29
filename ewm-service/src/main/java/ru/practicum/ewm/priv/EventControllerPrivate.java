package ru.practicum.ewm.priv;

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
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.ParticipationRequestDto;
import ru.practicum.ewm.request.UpdateEventUserRequest;

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
    public List<EventShortDto> getEvents(@PathVariable("userId") @Positive Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, EVENT_ID_FIELD_NAME));
        return userEventService.getEvents(userId, pageable);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable("userId") @Positive Long userId,
                                 @RequestBody NewEventDto event) {
        return userEventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("userId") @Positive Long userId,
                                 @PathVariable("eventId") @Positive Long eventId) {
        return userEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") @Positive Long userId,
                                    @PathVariable("eventId") @Positive Long eventId,
                                    @RequestBody UpdateEventUserRequest event) {
        return userEventService.updateEvent(userId, eventId, event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipation(@PathVariable("userId")@Positive Long userId,
                                                                     @PathVariable("eventId") @Positive Long eventId) {
        return userEventService.getRequestsForParticipation(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable("userId") @Positive Long userId,
                                                              @PathVariable("eventId") @Positive Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        return userEventService.updateRequestStatus(userId, eventId, request);
    }
}