package ru.practicum.services.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.AdminEventsFindParameters;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto addEvent(Long userId, NewEventDto event);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest event);

    List<ParticipationRequestDto> getRequestsForParticipation(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest request);

    List<EventFullDto> getEventsByAdmin(AdminEventsFindParameters parameters, Pageable pageable);

    EventFullDto getEventByAdmin(Long eventId);

    EventFullDto patchEventByAdmin(Long eventId, UpdateEventAdminRequest newEvent);
}