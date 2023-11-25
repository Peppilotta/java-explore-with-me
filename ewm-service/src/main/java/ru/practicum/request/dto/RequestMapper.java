package ru.practicum.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequester().getId());
        dto.setCreated(request.getCreated());
        dto.setStatus(request.getStatus());

        return dto;
    }

    public List<ParticipationRequestDto> toDtos(List<Request> requests) {
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }
}