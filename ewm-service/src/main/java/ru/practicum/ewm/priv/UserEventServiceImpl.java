package ru.practicum.ewm.priv;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.UpdateEventUserRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserEventServiceImpl implements UserEventService {

    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        return new ArrayList<>();
    }

    public EventFullDto addEvent(Long userId, NewEventDto event) {
        return new EventFullDto();
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        return new EventFullDto();
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest event) {
        return new EventFullDto();
    }

    public List<ParticipationRequestDto> getRequestsForParticipation(Long userId, Long eventId) {
        return new ArrayList<>();
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        return new EventRequestStatusUpdateResult();
    }
}