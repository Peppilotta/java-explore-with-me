package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.location.dto.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.dto.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final RequestRepository requestRepository;

    private final LocationMapper locationMapper;

    public Event toEventFromNew(NewEventDto newEvent, Location location, Category category) {
        boolean requestModeration = newEvent.getRequestModeration();
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .category(category)
                .description(newEvent.getDescription())
                .eventDate(newEvent.getEventDate())
                .initiator(userMapper.fromShort(newEvent.getInitiator()))
                .createdOn(newEvent.getCreatedOn())
                .location(location)
                .state(EventLifeState.PENDING)
                .paid(newEvent.getPaid())
                .participantLimit(newEvent.getParticipantLimit())
                .requestModeration(requestModeration)
                .title(newEvent.getTitle())
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED);
        return EventShortDto.builder()
                .id(eventId)
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .location(locationMapper.toDto(event.getLocation()))
                .initiator(userMapper.toUserShort(event.getInitiator()))
                .build();
    }

    public List<EventShortDto> toShortDtos(List<Event> events) {
        return events.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    public List<EventFullDto> toFullDtos(List<Event> events) {
        return events.stream().map(this::toFullDto).collect(Collectors.toList());
    }

    public EventFullDto toFullDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED);
        return EventFullDto.builder()
                .id(eventId)
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .paid(event.getPaid())
                .location(locationMapper.toDto(event.getLocation()))
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreatedOn())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShort(event.getInitiator()))
                .build();
    }

    public Event fromUpdatedByUser(Event event, UpdateEventUserRequest eventUpdate) {
        String annotation = eventUpdate.getAnnotation();
        if (!Objects.isNull(annotation)) {
            event.setAnnotation(annotation);
        }

        String description = eventUpdate.getDescription();
        if (!Objects.isNull(description)) {
            event.setDescription(description);
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
        ReviewAction action = eventUpdate.getStateAction();
        if (!Objects.isNull(action)) {
            event.setState(Objects.equals(action, ReviewAction.CANCEL_REVIEW)
                    ? EventLifeState.CANCELED
                    : EventLifeState.PENDING);
        }
        String title = eventUpdate.getTitle();
        if (!Objects.isNull(title)) {
            event.setTitle(title);
        }
        return event;
    }

    public Event fromUpdatedByAdmin(Event event, UpdateEventAdminRequest eventUpdate) {
        String annotation = eventUpdate.getAnnotation();
        if (!Objects.isNull(annotation)) {
            event.setAnnotation(annotation);
        }

        String description = eventUpdate.getDescription();
        if (!Objects.isNull(description)) {
            event.setDescription(description);
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
        StateAction stateAction = eventUpdate.getStateAction();
        if (!Objects.isNull(stateAction)) {
            event.setState(Objects.equals(stateAction, StateAction.PUBLISH_EVENT)
                    ? EventLifeState.PUBLISHED
                    : EventLifeState.CANCELED);
        }
        String title = eventUpdate.getTitle();
        if (!Objects.isNull(title)) {
            event.setTitle(title);
        }
        return event;
    }
}