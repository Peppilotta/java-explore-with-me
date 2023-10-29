package ru.practicum.ewm.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RequestServicePrivateImpl implements RequestServicePrivate {

    public List<ParticipationRequestDto> getRequests(Long userId) {
        return new ArrayList<>();
    }

    public ParticipationRequestDto getRequest(Long userId, Long eventId) {
        return new ParticipationRequestDto();
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long eventId) {
        return new ParticipationRequestDto();
    }
}