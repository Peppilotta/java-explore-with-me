package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.editing.dto.EventCorrectionDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.dto.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final RequestRepository requestRepository;

    private final LocationMapper locationMapper;

    public Event toEventFromNew(NewEventDto newEvent, Location location, Category category) {
        boolean requestModeration = newEvent.getRequestModeration();
        Event event = new Event();
        event.setAnnotation(newEvent.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate());
        event.setInitiator(userMapper.fromShort(newEvent.getInitiator()));
        event.setCreatedOn(newEvent.getCreatedOn());
        event.setLocation(location);
        event.setState(EventLifeState.PENDING);
        event.setPaid(newEvent.getPaid());
        event.setParticipantLimit(newEvent.getParticipantLimit());
        event.setRequestModeration(requestModeration);
        event.setViews(0L);
        event.setTitle(newEvent.getTitle());

        return event;
    }

    public EventCorrectionDto toCorrection(Event event) {
        return new EventCorrectionDto(event.getId(), event.getTitle(), event.getAnnotation(), event.getDescription());
    }

    public EventShortDto toShortDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(eventId);
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(categoryMapper.toDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        eventShortDto.setLocation(locationMapper.toDto(event.getLocation()));
        eventShortDto.setInitiator(userMapper.toUserShort(event.getInitiator()));

        return eventShortDto;
    }

    public List<EventShortDto> toShortDtos(List<Event> events) {
        return events.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    public EventFullDto toFullDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(eventId);
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setLocation(locationMapper.toDto(event.getLocation()));
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setCategory(categoryMapper.toDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(userMapper.toUserShort(event.getInitiator()));
        return eventFullDto;
    }

    public Event fromUpdated(Event event, UpdateEventRequest eventUpdate) {
        if (!Objects.isNull(eventUpdate.getAnnotation())) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (!Objects.isNull(eventUpdate.getDescription())) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (!Objects.isNull(eventUpdate.getPaid())) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (!Objects.isNull(eventUpdate.getParticipantLimit())) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (!Objects.isNull(eventUpdate.getRequestModeration())) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }

        if (!Objects.isNull(eventUpdate.getTitle())) {
            event.setTitle(eventUpdate.getTitle());
        }
        return event;
    }

    public Event fromUpdatedByUser(Event event, UpdateEventUserRequest eventUpdate) {

        Event updated = fromUpdated(event, eventUpdate);
        ReviewAction updatedAction = eventUpdate.getStateAction();

        if (!Objects.isNull(updatedAction)) {
            log.debug("        ReviewAction is not null ");
            if (Objects.equals(eventUpdate.getStateAction(), ReviewAction.CANCEL_REVIEW)) {
                updated.setState(EventLifeState.CANCELED);
            }
            if (Objects.equals(eventUpdate.getStateAction(), ReviewAction.SEND_TO_REVIEW)) {
                updated.setState(EventLifeState.PENDING);
            }
        }
        return updated;
    }

    public Event fromUpdatedByAdmin(Event event, UpdateEventAdminRequest eventUpdate) {
        Event updated = fromUpdated(event, eventUpdate);
        EventLifeState initialState = event.getState();

        EventLifeState state = EventLifeState.PUBLISHED;
        if (!Objects.isNull(eventUpdate.getStateAction())) {
            StateAction stateAction = eventUpdate.getStateAction();
            if (Objects.equals(stateAction, StateAction.REJECT_EVENT)) {
                state = EventLifeState.CANCELED;
            }
            if (Objects.equals(stateAction, StateAction.GET_NOTED)) {
                state = EventLifeState.NOTED;
            }
            updated.setState(state);
        }
        EventLifeState changedState = event.getState();
        if (!Objects.equals(initialState, changedState) && Objects.equals(changedState, EventLifeState.PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }

        return event;
    }
}