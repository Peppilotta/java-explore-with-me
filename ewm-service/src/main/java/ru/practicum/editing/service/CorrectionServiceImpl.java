package ru.practicum.editing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.editing.dto.CorrectionAuthor;
import ru.practicum.editing.dto.CorrectionDto;
import ru.practicum.editing.dto.CorrectionMapper;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.NewCorrectionDtoWithEnum;
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.editing.model.Correction;
import ru.practicum.editing.storage.CorrectionRepository;
import ru.practicum.editing.storage.CorrectionSpecification;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        log.info("Get corrections from userId={} to eventId={}", userId, eventId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        checkUserEvent(userId, eventId);
        return correctionMapper
                .toDtos(correctionRepository.findAll(correctionSpecification.get(eventId, eventFields, revisionStates)));
    }

    @Override
    public List<CorrectionDto> getCorrectionForEventAdmin(Long eventId,
                                                          List<EventField> eventFields,
                                                          List<RevisionState> revisionStates) {
        checkEventExistence(eventId);
        log.info("Get corrections by admin to eventId={}", eventId);
        return correctionMapper
                .toDtos(correctionRepository.findAll(correctionSpecification.get(eventId, eventFields, revisionStates)));
    }

    @Override
    public List<CorrectionDto> postNotesByAdmin(Long eventId, Map<String, String> correctionDtos) {
        log.info("Post notes by admin to eventId={}", eventId);
        checkEventExistence(eventId);

        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkEventStatus(event.getState());
        List<NewCorrectionDtoWithEnum> notesFromAdmin = convertAndCheck(correctionDtos);
        List<Correction> corrections = new ArrayList<>();
        for (NewCorrectionDtoWithEnum newCorrection : notesFromAdmin) {
            EventField eventField = newCorrection.getEventField();
            log.debug("          Event field = {}", eventField);
            Boolean repeated = correctionRepository.existsByEventIdAndEventField(eventId, eventField);
            log.debug("          Repeated correction - {}", repeated);
            Correction correction = (Boolean.TRUE.equals(repeated)
                    ? correctionRepository.findByEventIdAndEventField(eventId, eventField)
                    : new Correction());
            String fieldContent = getEventFieldContent(event, eventField);

            correction.setEvent(event);

            correction.setEventField(eventField);

            correction.setAdminNote(newCorrection.getContent());

            if (!Boolean.TRUE.equals(repeated)) {
                correction.setBefore(fieldContent);
                correction.setAfter(fieldContent);
            }

            correction.setCorrectionAuthor(CorrectionAuthor.ADMIN_ONLY_NOTE);

            correction.setState(Boolean.TRUE.equals(repeated) ? RevisionState.REPEATED : RevisionState.INITIAL);

            log.debug("                  Correction = {}", newCorrection);
            corrections.add(correction);
        }

        return correctionMapper.toDtos(correctionRepository.saveAll(corrections));
    }

    @Override
    public void saveCorrectionForEditedFields(Long eventId, List<EventField> fields, CorrectionAuthor author) {
        log.info("Save correction after fields editing");
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
                log.debug("                  Event field = {}", eventField);
                log.debug("                  Correction  = {}", correction);
                corrections.add(correction);
            }
        }
        correctionRepository.saveAll(corrections);
    }

    @Override
    @Transactional
    public List<CorrectionDto> reviewCorrectionByAdmin(Long eventId,
                                                       List<EventField> eventFields) {
        log.info("Set corrections RESOLVED for eventId={}", eventId);
        checkEventExistence(eventId);
        if (Objects.isNull(eventFields) || eventFields.isEmpty()) {
            apiErrorBadRequest.setMessage("Bad corrections list");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
        eventFields.forEach(e -> checkCorrectionExists(eventId, e));
        List<Correction> corrections = correctionRepository.findAllByEventFieldsAndEventId(eventFields, eventId);
        corrections.forEach(c -> c.setState(RevisionState.RESOLVED));
        List<Correction> allCorrectionForEvent = correctionRepository.findAllByEventId(eventId);
        boolean resolved = true;
        for (Correction correction : allCorrectionForEvent) {
            if (!Objects.equals(correction.getState(), RevisionState.RESOLVED)) {
                resolved = false;
                break;
            }
        }
        List<Correction> output = new ArrayList<>(correctionRepository.saveAll(corrections));

        if (resolved) {
            Event event = eventRepository.findById(eventId).get();
            event.setState(EventLifeState.PUBLISHED);
            eventRepository.save(event);
            log.debug("          Event published, id = {}", eventId);

            correctionRepository.deleteAll(allCorrectionForEvent);
            log.debug("          All corrections deleted");
        }
        return correctionMapper.toDtos(output);
    }

    private List<NewCorrectionDtoWithEnum> convertAndCheck(Map<String, String> corrections) {
        if (Objects.isNull(corrections) || corrections.isEmpty()) {
            apiErrorBadRequest.setMessage("Bad corrections list");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
        List<NewCorrectionDtoWithEnum> withEnum = new ArrayList<>();
        for (String field : corrections.keySet()) {
            if (EventField.existsByName(field)) {
                withEnum.add(new NewCorrectionDtoWithEnum(EventField.findByName(field), corrections.get(field)));
            } else {
                apiErrorBadRequest.setMessage("Wrong field name = " + field);
                apiErrorBadRequest.setTimestamp(LocalDateTime.now());
                throw new BadRequestException(apiErrorBadRequest);
            }
        }
        log.debug("         Fields converted");
        return withEnum;
    }

    private void checkCorrectionExists(Long eventId, EventField eventFields) {
        if (Boolean.FALSE.equals(correctionRepository.existsByEventIdAndEventField(eventId, eventFields))) {
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
        log.debug("         Get field content as string");
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