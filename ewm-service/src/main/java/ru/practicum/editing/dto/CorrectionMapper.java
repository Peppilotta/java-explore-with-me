package ru.practicum.editing.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.editing.model.Correction;
import ru.practicum.event.dto.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CorrectionMapper {

    private final EventMapper eventMapper;

    public CorrectionDto toDto(Correction correction) {
        CorrectionDto dto = new CorrectionDto();
        dto.setId(correction.getId());
        dto.setEvent(eventMapper.toCorrection(correction.getEvent()));
        dto.setEventField(correction.getEventField());
        dto.setBefore(correction.getBefore());
        dto.setAfter(correction.getAfter());
        dto.setAdminNote(correction.getAdminNote());
        dto.setCorrectionAuthor(correction.getCorrectionAuthor());
        dto.setState(correction.getState());
        return dto;
    }

    public List<CorrectionDto> toDtos(List<Correction> corrections) {
        return corrections.stream().map(this::toDto).collect(Collectors.toList());
    }

}
