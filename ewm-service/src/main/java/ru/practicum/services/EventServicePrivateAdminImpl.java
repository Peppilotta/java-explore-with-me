package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.editing.dto.CorrectionAuthor;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.service.CorrectionService;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.event.dto.AdminEventsFindParameters;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.StateAction;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventRequest;
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
import ru.practicum.services.interfaces.EventService;
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
public class EventServicePrivateAdminImpl implements EventService {

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

    private final CorrectionService correctionService;

    private final ApiError apiErrorConflict = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
            "For the requested operation the conditions are not met.",
            "", LocalDateTime.now());

    private final ApiError apiError = new ApiError(ErrorStatus.E_404_NOT_FOUND.getValue(),
            "The required object was not found.",
            "", LocalDateTime.now());

    private final ApiError apiErrorBadRequest = new ApiError(ErrorStatus.E_400_BAD_REQUEST.getValue(),
            "Incorrectly made request.", "", LocalDateTime.now());

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Pageable pageable) {
        log.info("Get events of user with id={}", userId);
        checkUserExistence(userId);
        return eventRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(AdminEventsFindParameters parameters, Pageable pageable) {
        log.info("Request for list Events according parameters {}", parameters);
        checkUsersInParameters(parameters.getUsers());
        checkCategoriesInParameters(parameters.getCategories());
        return eventRepository.findAll(eventSpecification.getEventsByParameters(parameters), pageable)
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        log.info("Get request for events of user with id={}", userId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkUserEvent(userId, event.getInitiator().getId());
        return eventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto getEventByAdmin(Long eventId) {
        log.info("Get request for  Event id = {}", eventId);
        checkEventExistence(eventId);
        return eventMapper.toFullDto(eventRepository.findById(eventId).orElseGet(Event::new));
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto event) {
        log.info("Create event from user with id={}. Event = {}", userId, event);
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
        checkLocation(event.getLocation());
        return eventMapper.toFullDto(eventRepository.save(eventMapper.toEventFromNew(event,
                saveTestedLocation(event.getLocation()),
                category)));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updates) {
        log.info("Update request of user with id={} for event with id={} and updates = {}",
                userId, eventId, updates);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkEventStatus(event.getState());
        checkUserEvent(userId, event.getInitiator().getId());

        UserShortDto initiator = updates.getInitiator();
        if (!Objects.isNull(initiator)) {
            checkUserEvent(userId, initiator.getId());
            event.setInitiator(userMapper.fromShort(initiator));
        }

        Event updatedCategoryDateLocation = updateCategoryDateLocation(event, updates);

        Event saved = eventRepository.save(eventMapper.fromUpdatedByUser(updatedCategoryDateLocation, updates));

        if (Objects.equals(saved.getState(), EventLifeState.NOTED)) {
            log.debug("     Send updates to corrections by user");
            correctionService.saveCorrectionForEditedFields(eventId, getUpdatedFields(updates), CorrectionAuthor.USER);
        }
        return eventMapper.toFullDto(saved);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updates) {
        log.info("Update event with id={} by admin. Updates = {}", eventId, updates);
        checkEventExistence(eventId);
        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        checkStateForAdminUpdate(updates.getStateAction(), event.getState());

        Event updatedCategoryDateLocation = updateCategoryDateLocation(event, updates);

        Event saved = eventRepository.save(eventMapper.fromUpdatedByAdmin(updatedCategoryDateLocation, updates));

        if (Objects.equals(event.getState(), EventLifeState.NOTED)) {
            log.debug("     Send updates to corrections by Admin");

            correctionService.saveCorrectionForEditedFields(eventId,
                    getUpdatedFields(updates), CorrectionAuthor.ADMIN);
        }
        return eventMapper.toFullDto(saved);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForParticipation(Long userId, Long eventId) {
        log.info("Request of participation from user with id={} for event with id={}", userId, eventId);
        checkUserExistence(userId);
        checkEventExistence(eventId);
        return requestMapper.toDtos(requestRepository.findByOwnerIdAndEventId(userId, eventId));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest requestUpdates) {
        log.info("Update requests of participation from user with id={} for event with id={} and updates = {}",
                userId, eventId, requestUpdates);
        checkUserExistence(userId);
        checkEventExistence(eventId);

        List<Long> requestIds = requestUpdates.getRequestIds();
        RequestStatus newStatus = requestUpdates.getStatus();

        checkUpdates(requestIds, newStatus);

        Event event = eventRepository.findById(eventId).orElseGet(Event::new);
        Long limit = Long.valueOf(event.getParticipantLimit());
        Long confirmed = requestRepository.getAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if (limit == 0 && event.getRequestModeration().equals(false)) {
            List<ParticipationRequestDto> dto = requestMapper
                    .toDtos(changeStatusForRequests(requestIds, RequestStatus.CONFIRMED));
            return new EventRequestStatusUpdateResult(dto, new ArrayList<>());
        }
        List<Long> confirmedIds = new ArrayList<>();
        List<Long> rejectedIds = new ArrayList<>();

        if (Objects.equals(newStatus, RequestStatus.REJECTED)) {
            rejectedIds.addAll(requestIds);
            List<ParticipationRequestDto> dto = requestMapper
                    .toDtos(changeStatusForRequests(rejectedIds, RequestStatus.REJECTED));
            return new EventRequestStatusUpdateResult(new ArrayList<>(), dto);
        }
        boolean limitReached = Objects.equals(limit, confirmed);
        if (limitReached) {
            apiErrorConflict.setMessage("Limit reached yet");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
        for (Long id : requestIds) {
            Long confirmedBefore = requestRepository.getAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (Objects.equals(limit, confirmedBefore)) {
                rejectedIds.add(id);
                limitReached = true;
            } else {
                if (limitReached) {
                    rejectedIds.add(id);
                } else {
                    confirmedIds.add(id);
                }
            }
        }

        if (limitReached) {
            changeStatusForRequests(requestRepository
                    .findAllByEventIdAndStatus(eventId, RequestStatus.PENDING), RequestStatus.REJECTED);
        }
        return new EventRequestStatusUpdateResult(
                requestMapper.toDtos(changeStatusForRequests(confirmedIds, RequestStatus.CONFIRMED)),
                requestMapper.toDtos(changeStatusForRequests(rejectedIds, RequestStatus.REJECTED)));
    }

    private Event updateCategoryDateLocation(Event event, UpdateEventRequest updates) {
        Long categoryId = updates.getCategory();
        if (!Objects.isNull(categoryId)) {
            checkCategoryExistence(categoryId);
            event.setCategory(categoryRepository.findById(categoryId).orElseGet(Category::new));
        }

        LocalDateTime date = updates.getEventDate();
        if (!Objects.isNull(date)) {
            checkEventTime(date);
            event.setEventDate(date);
        }
        LocationDto locationDto = updates.getLocation();
        if (!Objects.isNull(locationDto)) {
            event.setLocation(saveTestedLocation(locationDto));
        }

        return event;
    }

    private List<EventField> getUpdatedFields(UpdateEventRequest updatedEvent) {
        List<EventField> eventFields = new ArrayList<>();

        if (!Objects.isNull(updatedEvent.getCategory())) {
            eventFields.add(EventField.CATEGORY);
        }

        if (!Objects.isNull(updatedEvent.getEventDate())) {
            eventFields.add(EventField.EVENT_DATE);
        }

        if (!Objects.isNull(updatedEvent.getLocation())) {
            eventFields.add(EventField.LOCATION);
        }

        if (!Objects.isNull(updatedEvent.getAnnotation())) {
            eventFields.add(EventField.ANNOTATION);
        }

        if (!Objects.isNull(updatedEvent.getDescription())) {
            eventFields.add(EventField.DESCRIPTION);
        }

        if (!Objects.isNull(updatedEvent.getParticipantLimit())) {
            eventFields.add(EventField.PARTICIPANT_LIMIT);
        }

        if (!Objects.isNull(updatedEvent.getPaid())) {
            eventFields.add(EventField.PAID);
        }

        if (!Objects.isNull(updatedEvent.getRequestModeration())) {
            eventFields.add(EventField.REQUEST_MODERATION);
        }
        return eventFields;
    }

    private List<Request> changeStatusForRequests(List<Long> requestIds, RequestStatus status) {
        List<Request> requests = requestRepository.findAllByIds(requestIds);
        requests.forEach(r -> r.setStatus(status));
        return requestRepository.saveAll(requests);
    }

    private void checkLocation(LocationDto locationDto) {
        if (Objects.isNull(locationDto.getLat()) || Objects.isNull(locationDto.getLon())) {
            apiErrorBadRequest.setMessage("Bad location field ");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
    }

    private void checkUpdates(List<Long> requestIds, RequestStatus newStatus) {
        if (Objects.isNull(requestIds)
                || requestIds.isEmpty()
                || !(Objects.equals(newStatus, RequestStatus.CONFIRMED)
                || Objects.equals(newStatus, RequestStatus.REJECTED))) {
            apiErrorBadRequest.setMessage("Wrong data for updates");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
        }
        requestIds.forEach(this::checkRequestStatus);
    }

    private void checkUsersInParameters(List<Long> users) {
        if (!Objects.isNull(users) && !users.isEmpty()) {
            apiErrorBadRequest.setMessage("Bad users parameter");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            users.forEach(u ->
                    userRepository.findById(u).orElseThrow(() ->
                            new BadRequestException(apiErrorBadRequest)));
        }
    }

    private void checkCategoriesInParameters(List<Long> categories) {
        if (!Objects.isNull(categories) && !categories.isEmpty()) {
            apiErrorBadRequest.setMessage("Bad categories parameter");
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            categories.forEach(u ->
                    categoryRepository.findById(u).orElseThrow(() ->
                            new BadRequestException(apiErrorBadRequest)));
        }
    }

    private void checkStateForAdminUpdate(StateAction action, EventLifeState lifeState) {
        if ((Objects.equals(action, StateAction.PUBLISH_EVENT) && !Objects.equals(lifeState, EventLifeState.PENDING))
                || (Objects.equals(action, StateAction.REJECT_EVENT) && Objects.equals(lifeState, EventLifeState.PUBLISHED))) {
            apiErrorConflict.setMessage("Illegal actions with State");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
        }
    }

    private void checkEventTime(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            apiErrorBadRequest.setMessage("Field: eventDate. Error: Time must be in the future. Value:" + eventDate);
            apiErrorBadRequest.setTimestamp(LocalDateTime.now());
            throw new BadRequestException(apiErrorBadRequest);
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

    private void checkRequestStatus(Long id) {
        if (!requestRepository.existsById(id)) {
            apiError.setMessage("Request with id=" + id + " does not exists.");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
        if (!Objects.equals(requestRepository.findById(id).orElseGet(Request::new).getStatus(), RequestStatus.PENDING)) {
            apiErrorConflict.setMessage("Only Pending request can be updated.");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorConflict);
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

    private void checkCategoryExistence(Long id) {
        if (!categoryRepository.existsById(id)) {
            apiError.setMessage("Category with id=" + id + " does not exists.");
            apiError.setTimestamp(LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkEventStatus(EventLifeState state) {
        if (Objects.equals(state, EventLifeState.PUBLISHED)) {
            apiErrorConflict.setMessage("Event must not be published");
            apiErrorConflict.setTimestamp(LocalDateTime.now());
            throw new ConflictException(apiErrorBadRequest);
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