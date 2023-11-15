package ru.practicum.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventsFindParameters;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ReviewAction;
import ru.practicum.event.dto.StateAction;
import ru.practicum.event.dto.UpdateEventDtoByAdmin;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.EventSpecification;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.storage.LocationRepository;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.model.Request;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final EventSpecification eventSpecification;

    private final RequestMapper requestMapper;

    private final EventMapper eventMapper;

    private final UserMapper userMapper;

    private final LocationMapper locationMapper;

    private final ApiError apiErrorConflict = ApiError.builder()
            .message("")
            .reason("For the requested operation the conditions are not met.")
            .status(ErrorStatus.E_409_CONFLICT.getValue())
            .timestamp(LocalDateTime.now())
            .build();

    private final ApiError apiError = ApiError.builder()
            .message("")
            .reason("The required object was not found.")
            .status(ErrorStatus.E_404_NOT_FOUND.getValue())
            .timestamp(LocalDateTime.now())
            .build();

    private final ApiError apiErrorBadRequest = ApiError.builder()
            .message("")
            .reason("Incorrectly made request.")
            .status(ErrorStatus.E_400_BAD_REQUEST.getValue())
            .timestamp(LocalDateTime.now())
            .build();

    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        log.info("Get request for events of user with id={}", userId);
        checkUserExistence(userId);
        return eventRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto addEvent(Long userId, NewEventDto event) {
        log.info("Create request  of user with id={} for event = {}", userId, event);
        checkEventTime(event.getEventDate());
        checkUserExistence(userId);
        if (Objects.isNull(event.getInitiator())) {
            event.setInitiator(userRepository.findByIdToShort(userId));
        } else {
            checkUserEvent(userId, event.getInitiator().getId());
        }
        Long categoryId = event.getCategory();
        checkCategoryExistence(categoryId);
        Category category = categoryRepository.findById(categoryId).orElseGet(Category::new);
        event.setCreatedOn(LocalDateTime.now());

        return eventMapper.toFullDto(eventRepository.save(eventMapper.toEventFromNew(event,
                saveTestedLocation(event.getLocation()),
                category)));
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        log.info("Get request for events of user with id={}", userId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkUserEvent(userId, event.getInitiator().getId());
        return eventMapper.toFullDto(event);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventUpdate) {
        log.info("Update request of user with id={} for event with id={} and updates = {}",
                userId, eventId, eventUpdate);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkEventStatus(event.getState());
        checkUserEvent(userId, event.getInitiator().getId());
        UserShortDto initiator = eventUpdate.getInitiator();
        if (!Objects.isNull(initiator)) {
            checkUserEvent(userId, initiator.getId());
            event.setInitiator(userMapper.fromShort(initiator));
        }
        event.setAnnotation(eventUpdate.getAnnotation());
        event.setDescription(eventUpdate.getDescription());
        LocalDateTime date = eventUpdate.getEventDate();
        checkEventTime(date);
        if (!Objects.isNull(date)) {
            event.setEventDate(date);
        }
        LocationDto locationDto = eventUpdate.getLocation();
        if (!Objects.isNull(locationDto)) {
            event.setLocation(saveTestedLocation(locationDto));
        }
        Boolean paid = eventUpdate.getPaid();
        if (!Objects.isNull(paid)) {
            event.setPaid(paid);
        }
        Integer participantLimit = eventUpdate.getParticipantLimit();
        if (!Objects.isNull(participantLimit)) {
            event.setParticipantLimit(participantLimit);
        }
        Boolean requestModeration = eventUpdate.getRequestModeration();
        if (!Objects.isNull(requestModeration)) {
            event.setRequestModeration(requestModeration);
        }
        ReviewAction stateAction = eventUpdate.getStateAction();
        if (!Objects.isNull(stateAction)) {
            event.setState(Objects.equals(stateAction, ReviewAction.CANCEL_REVIEW)
                    ? EventLifeState.CANCELED
                    : EventLifeState.PENDING);
        }
        String title = eventUpdate.getTitle();
        if (!Objects.isNull(title)) {
            event.setTitle(title);
        }

        return eventMapper.toFullDto(eventRepository.save(event));
    }

    public List<ParticipationRequestDto> getRequestsForParticipation(Long userId, Long eventId) {
        log.info("Request of participation from user with id={} for event with id={}", userId, eventId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        return requestMapper.toDtos(requestRepository.findByOwnerIdAndEventId(userId, eventId));
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest requestUpdates) {
        log.info("Update request of participation from user with id={} for event with id={} and updates = {}",
                userId, eventId, requestUpdates);
        List<Long> requestIds = requestUpdates.getRequestIds();
        if (Objects.isNull(requestIds)) {
            return new EventRequestStatusUpdateResult();
        }
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        Long limit = Long.valueOf(event.getParticipantLimit());
        if (limit == 0 || event.getRequestModeration().equals(false)) {
            return new EventRequestStatusUpdateResult();
        }
        RequestStatus newStatus = requestUpdates.getStatus();
        List<Request> requests = requestRepository.findAllByIds(requestIds);
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (!Objects.equals(request.getStatus(), RequestStatus.PENDING)) {
                checkRequestIntegrity("Only Pending request can be updated.");
            }
            Long confirmed = requestRepository
                    .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED);
            if (Objects.equals(limit, confirmed)) {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                checkRequestIntegrity("Requests limit to event with id=" + eventId + " is reached.");
            } else {
                request.setStatus(newStatus);
                requestRepository.save(request);
                if (Objects.equals(newStatus, RequestStatus.CONFIRMED)) {
                    confirmedRequests.add(request);
                } else {
                    rejectedRequests.add(request);
                }
            }
        }

        return new EventRequestStatusUpdateResult(requestMapper.toDtos(confirmedRequests),
                requestMapper.toDtos(rejectedRequests));
    }

    public List<EventFullDto> getEventsByAdmin(EventsFindParameters parameters, Pageable pageable) {
        log.info("Request for list Events according parameters {}", parameters);
        return eventRepository.findAll(eventSpecification.getEventsByParameters(parameters), pageable)
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventByAdmin(Long eventId) {
        checkEventExistence(eventId);
        return eventMapper.toFullDto(eventRepository.findById(eventId).orElseGet(Event::new));
    }

    public EventFullDto patchEventByAdmin(Long eventId, UpdateEventDtoByAdmin newEvent) {
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        event.setAnnotation(newEvent.getAnnotation());
        Long categoryId = newEvent.getCategory();
        checkCategoryExistence(categoryId);
        event.setCategory(categoryRepository.findById(categoryId).orElseGet(Category::new));
        event.setDescription(newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate());
        LocationDto locationDto = newEvent.getLocation();
        event.setLocation(saveTestedLocation(locationDto));
        event.setPaid(newEvent.getPaid());
        event.setParticipantLimit(newEvent.getParticipantLimit());
        event.setRequestModeration(newEvent.getRequestModeration());
        event.setState(Objects.equals(newEvent.getStateAction(), StateAction.PUBLISH_EVENT)
                ? EventLifeState.PUBLISHED
                : EventLifeState.CANCELED);
        event.setTitle(newEvent.getTitle());

        return eventMapper.toFullDto(eventRepository.save(event));
    }

    private void checkEventTime(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            apiError.setMessage("Field: eventDate. Error: Time must be in the future. Value:" + eventDate);
            apiError.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiError);
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

    private void checkRequestIntegrity(String message) {
        apiErrorConflict.setMessage(message);
        apiErrorConflict.setTimestamp(LocalDateTime.now());
        throw new NotFoundException(apiErrorConflict);
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

    private void checkCategoryExistence(Long id) {
        if (!categoryRepository.existsById(id)) {
            apiError.setMessage("Category with id=" + id + " does not exists.");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkEventStatus(EventLifeState state) {
        if (Objects.equals(state, EventLifeState.PUBLISHED)) {
            apiErrorBadRequest.setMessage("Event must not be published");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
    }

    private Location saveTestedLocation(LocationDto locationDto) {
        Float lat = locationDto.getLat();
        Float lon = locationDto.getLon();
        if (locationRepository.existsByLatAndLon(lat, lon)) {
            log.info("Location exists");
            return locationRepository.findByLatAndLon(lat, lon);
        } else {
            log.info("Location not exists");

            Location location = locationRepository.save(locationMapper.toLocation(locationDto));
            log.info("Location not exists, new location={}", location);
            return location;
        }
    }
}