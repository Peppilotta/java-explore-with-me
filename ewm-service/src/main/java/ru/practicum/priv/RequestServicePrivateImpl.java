package ru.practicum.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.model.Request;
import ru.practicum.request.storage.RequestRepository;
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

    public List<ParticipationRequestDto> getRequests(Long userId) {
        log.info("Get requests from user with id={} ", userId);
        checkUserExistence(userId);
        return requestMapper.toDtos(requestRepository.findByUserId(userId));
    }

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("Post new request from user with id={} to event with id={}", userId, eventId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Long confirmedRequests = requestRepository
                .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkEventAvailable(confirmedRequests, event, userId);
        Request request = Request.builder()
                .event(event)
                .requester(userRepository.findById(userId).orElseGet(User::new))
                .created(LocalDateTime.now())
                .status(Boolean.TRUE.equals(event.getRequestModeration())
                        ? RequestStatus.PENDING
                        : RequestStatus.CONFIRMED)
                .build();
        return requestMapper.toDto(requestRepository.save(request));
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Post Cancel to request from user with id={} to request with id={}", userId, requestId);
        checkUserExistence(userId);
        checkRequestExistence(requestId);
        Request request = requestRepository.findById(requestId).orElseGet(Request::new);
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    private void checkEventAvailable(Long confirmedRequests, Event event, Long userId) {
        Integer maxParticipants = event.getParticipantLimit();
        Long eventId = event.getId();
        if (maxParticipants > 0 && confirmedRequests >= maxParticipants
                || !requestRepository.findByUserIdAndEventId(userId, eventId).isEmpty()
                || !Objects.equals(event.getState(), EventLifeState.PUBLISHED)
                || Objects.equals(userId, event.getInitiator().getId())) {
            ApiError apiErrorConflict = ApiError.builder()
                    .message("could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement")
                    .reason("Integrity constraint has been violated.")
                    .status(ErrorStatus.E_409_CONFLICT.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiErrorConflict);
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