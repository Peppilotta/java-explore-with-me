package ru.practicum.priv;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.UpdateEventUserRequest;

import java.util.List;

public interface UserEventService {

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto addEvent(Long userId, NewEventDto event);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest event);

    List<ParticipationRequestDto> getRequestsForParticipation(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest request);
}