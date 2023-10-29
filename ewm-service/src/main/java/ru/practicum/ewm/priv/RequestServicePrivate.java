package ru.practicum.ewm.priv;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestServicePrivate {

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto getRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long eventId);
}