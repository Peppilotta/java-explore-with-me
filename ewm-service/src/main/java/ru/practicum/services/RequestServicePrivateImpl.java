package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.model.Request;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.services.interfaces.RequestServicePrivate;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServicePrivateImpl implements RequestServicePrivate {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final RequestMapper requestMapper;

    private final ApiError apiError = ApiError.builder()
            .message("")
            .reason("The required object was not found.")
            .status(ErrorStatus.E_404_NOT_FOUND.getValue())
            .timestamp(LocalDateTime.now())
            .build();

    private final ApiError apiErrorConflict = ApiError.builder()
            .message("")
            .reason("Integrity constraint has been violated.")
            .status(ErrorStatus.E_409_CONFLICT.getValue())
            .timestamp(LocalDateTime.now())
            .build();

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        log.info("Get requests from user with id={} to events of other user ", userId);
        checkUserExistence(userId);
        return requestMapper.toDtos(requestRepository.findByUserId(userId));
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("Post new request from user with id={} to event with id={}", userId, eventId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Integer confirmedRequests = requestRepository
                .getAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)
                .intValue();
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        Integer limit = event.getParticipantLimit();
        checkParticipantReached(event.getParticipantLimit(), confirmedRequests);
        checkRepeatRequest(userId, eventId);
        checkEventPublished(event.getState());
        checkEventIsOwn(userId, event.getInitiator().getId());
        RequestStatus status = RequestStatus.PENDING;
        if (limit == 0) {
            status = RequestStatus.CONFIRMED;
        } else {
            if (Boolean.FALSE.equals(event.getRequestModeration())) {
                status = RequestStatus.CONFIRMED;
            }
        }

        Request request = Request.builder()
                .event(event)
                .requester(userRepository.findById(userId).orElseGet(User::new))
                .created(LocalDateTime.now())
                .status(status)
                .build();
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Post Cancel to request from user with id={} to request with id={}", userId, requestId);
        checkUserExistence(userId);
        checkRequestExistence(requestId);
        Request request = requestRepository.findById(requestId).orElseGet(Request::new);
        checkUserIsRequester(userId, request.getRequester().getId());
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    private void checkUserIsRequester(Long userId, Long requesterId) {
        if (!Objects.equals(userId, requesterId)) {
            apiError.setMessage("User with id=" + userId
                    + " can't edit request with requester.id=" + requesterId);
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkParticipantReached(Integer maxParticipants, Integer confirmed) {
        if (maxParticipants > 0 && Objects.equals(confirmed, maxParticipants)) {
            apiErrorConflict.setMessage("ParticipantLimit is reached");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
    }

    private void checkRepeatRequest(Long userId, Long eventId) {
        if (!requestRepository.findByUserIdAndEventId(userId, eventId).isEmpty()) {
            apiErrorConflict.setMessage("Repeat request from user with id=" + userId + " to event with id=" + eventId);
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
    }

    private void checkEventPublished(EventLifeState state) {
        if (!Objects.equals(state, EventLifeState.PUBLISHED)) {
            apiErrorConflict.setMessage("Event not published yet.");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
    }

    private void checkEventIsOwn(Long userId, Long initiatorId) {
        if (Objects.equals(userId, initiatorId)) {
            apiErrorConflict.setMessage("Request to own event.");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
    }

    private void checkRequestExistence(Long id) {
        if (!requestRepository.existsById(id)) {
            apiError.setMessage("Request with id=" + id + " was not found");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
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
}