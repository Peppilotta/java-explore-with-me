package ru.practicum.editing.service;

import ru.practicum.editing.dto.CorrectionAuthor;
import ru.practicum.editing.dto.CorrectionDto;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.RevisionState;

import java.util.List;
import java.util.Map;

public interface CorrectionService {

    List<CorrectionDto> getCorrectionForEvent(Long userId, Long eventId,
                                              List<EventField> eventFields,
                                              List<RevisionState> revisionStates);

    List<CorrectionDto> getCorrectionForEventAdmin(Long eventId,
                                                   List<EventField> eventFields,
                                                   List<RevisionState> revisionStates);

    List<CorrectionDto> postNotesByAdmin(Long eventId, Map<String, String> correctionDto);

    void saveCorrectionForEditedFields(Long eventId, List<EventField> fields, CorrectionAuthor author);

    List<CorrectionDto> reviewCorrectionByAdmin(Long eventId,
                                                List<EventField> eventFields);

    Long checkEventIsCorrect(Long eventId);
}
