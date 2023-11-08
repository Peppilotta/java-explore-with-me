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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final RequestRepository requestRepository;

    private final LocationMapper locationMapper;

    public Event toEventFromNew(NewEventDto newEvent, Location location, Category category) {
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .category(category)
                .description(newEvent.getDescription())
                .eventDate(newEvent.getEventDate())
                .initiator(userMapper.fromShort(newEvent.getInitiator()))
                .location(location)
                .paid(newEvent.getPaid())
                .participantLimit(newEvent.getParticipantLimit())
                .requestModeration(newEvent.getRequestModeration())
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
}
