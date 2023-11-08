package ru.practicum.priv;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestServicePrivate {

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long eventId);
}