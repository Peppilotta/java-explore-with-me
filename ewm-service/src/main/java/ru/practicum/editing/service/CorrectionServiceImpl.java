package ru.practicum.editing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.editing.dto.CorrectionAuthor;
import ru.practicum.editing.dto.CorrectionDto;
import ru.practicum.editing.dto.CorrectionMapper;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.NewCorrectionDto;
import ru.practicum.editing.dto.NewCorrectionDtos;
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.editing.model.Correction;
import ru.practicum.editing.storage.CorrectionRepository;
import ru.practicum.editing.storage.CorrectionSpecification;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionRepository correctionRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CorrectionSpecification correctionSpecification;

    private final CorrectionMapper correctionMapper;

    private final EventMapper eventMapper;

    private final ApiError apiErrorConflict = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
            "For the requested operation the conditions are not met.",
            "", LocalDateTime.now());

    private final ApiError apiError = new ApiError(ErrorStatus.E_404_NOT_FOUND.getValue(),
            "The required object was not found.",
            "", LocalDateTime.now());

    private final ApiError apiErrorBadRequest = new ApiError(ErrorStatus.E_400_BAD_REQUEST.getValue(),
            "Incorrectly made request.", "", LocalDateTime.now());

    @Override
    public Long checkEventIsCorrect(Long eventId) {
        return 0L;
    }

    @Override
    public List<CorrectionDto> getCorrectionForEvent(Long userId, Long eventId,
                                                     List<EventField> eventFields,
                                                     List<RevisionState> revisionStates) {
        checkUserExistence(userId);
        checkEventExistence(eventId);
        checkUserEvent(userId, eventId);
      return   correctionMapper
              .toDtos(correctionRepository.findAll(correctionSpecification.get(eventId,eventFields,revisionStates)));
    }

    @Override
    public List<CorrectionDto> getCorrectionForEventAdmin(Long eventId,
                                                          List<EventField> eventFields,
                                                          List<RevisionState> revisionStates) {
        checkEventExistence(eventId);
        return   correctionMapper
                .toDtos(correctionRepository.findAll(correctionSpecification.get(eventId,eventFields,revisionStates)));
    }

    @Override
    public List<CorrectionDto> postNotesByAdmin(Long eventId, NewCorrectionDtos correctionDtos) {
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkEventStatus(event.getState());
        List<NewCorrectionDto> notesFromAdmin = correctionDtos.getCorrections();
        checkNewCorrections(notesFromAdmin);
        List<Correction> corrections = new ArrayList<>();
        for (NewCorrectionDto newCorrection : notesFromAdmin) {
            EventField eventField = newCorrection.getEventField();
            Boolean repeated = correctionRepository.existsByEventIdAndEventField(eventId, eventField);
            Correction correction = (Boolean.TRUE.equals(repeated)
                    ? correctionRepository.findByEventIdAndEventField(eventId, eventField)
                    : new Correction());
            StringBuilder before = new StringBuilder();
            String fieldContent = getEventFieldContent(event, eventField);
            correction.setAdminNote(newCorrection.getContent());
            correction.setCorrectionAuthor(CorrectionAuthor.ADMIN_ONLY_NOTE);
            correction.setEventField(eventField);
            correction.setAdminNote(newCorrection.getContent());
            if (Boolean.TRUE.equals(repeated)) {
                correction.setBefore(before.append(fieldContent).toString());
            }
            correction.setAfter(fieldContent);
            correction.setState(Boolean.TRUE.equals(repeated) ? RevisionState.REPEATED : RevisionState.INITIAL);
            corrections.add(correction);
        }

        return correctionMapper.toDtos(correctionRepository.saveAll(corrections));
    }

    @Override
    public void saveCorrectionForEditedFields(Long eventId, List<EventField> fields, CorrectionAuthor author) {
        List<Correction> corrections = new ArrayList<>();
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        for (EventField eventField : fields) {
            if (Boolean.TRUE.equals(correctionRepository.existsByEventIdAndEventField(eventId, eventField))) {
                String fieldContent = getEventFieldContent(event, eventField);
                Correction correction = correctionRepository.findByEventIdAndEventField(eventId, eventField);
                String after = correction.getAfter();
                correction.setBefore(after);
                correction.setAfter(fieldContent);
                correction.setState(RevisionState.EDITED);
                correction.setCorrectionAuthor(author);
                corrections.add(correction);
            }
        }
        correctionRepository.saveAll(corrections);
    }


    @Override
    @Transactional
    public List<CorrectionDto> reviewCorrectionByAdmin(Long eventId,
                                                       List<EventField> eventFields) {
        checkEventExistence(eventId);
        checkCorrectionsExists(eventId, eventFields);
        List<Correction> corrections = correctionRepository.findAllByEventFields(eventFields);
        corrections.forEach(c -> c.setState(RevisionState.RESOLVED));

        return correctionMapper.toDtos(correctionRepository.saveAll(corrections));
    }

    private void checkNewCorrections(List<NewCorrectionDto> corrections) {
        if (Objects.isNull(corrections) || corrections.isEmpty()) {
            apiErrorBadRequest.setMessage("Bad corrections list");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
    }

    private void checkCorrectionsExists(Long eventId, List<EventField> eventFields) {
        if (!correctionRepository.existsByEventIdAndEventFields(eventId, eventFields)) {
            apiErrorConflict.setMessage("Bad field list");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
    }
    private void checkEventStatus(EventLifeState state) {
        if (!Objects.equals(state, EventLifeState.NOTED)) {
            apiErrorConflict.setMessage("Event life status = " + state + ", cannot be corrected.");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
    }

    private String getEventFieldContent(Event event, EventField eventField) {
        switch (eventField) {
            case ANNOTATION: {
                return event.getAnnotation();
            }
            case DESCRIPTION: {
                return event.getDescription();
            }
            case CATEGORY: {
                return event.getCategory().toString();
            }
            case EVENT_DATE: {
                return event.getEventDate().toString();
            }
            case LOCATION: {
                return event.getLocation().toString();
            }
            case PAID: {
                return event.getPaid().toString();
            }
            case PARTICIPANT_LIMIT: {
                return event.getParticipantLimit().toString();
            }
            case REQUEST_MODERATION: {
                return event.getRequestModeration().toString();
            }
            case TITLE: {
                return event.getTitle();
            }
            default: {
                return "";
            }
        }
    }

    private void checkUserExistence(Long id) {
        if (!userRepository.existsById(id)) {
            apiError.setMessage("User with id=" + id + " does not exists.");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkEventExistence(Long id) {
        if (!eventRepository.existsById(id)) {
            apiError.setMessage("Event with id=" + id + " does not exists.");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkUserEvent(Long userId, Long userIdFromEvent) {
        if (!Objects.equals(userId, userIdFromEvent)) {
            apiError.setMessage("User with id=" + userId
                    + " can't edit event with initiator.id=" + userIdFromEvent);
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }
}