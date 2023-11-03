package ru.practicum.ewm.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.storage.RequestRepository;
import ru.practicum.ewm.user.dto.UserMapper;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final RequestRepository requestRepository;

    private final LocationMapper locationMapper;

    public EventShortDto toShortDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED.getValue());
        return EventShortDto.builder()
                .id(eventId)
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShort(event.getInitiator()))
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        Long eventId = event.getId();
        Long confirmedRequests = requestRepository
                .getConfirmedRequestsForEventWithId(eventId, RequestStatus.CONFIRMED.getValue());
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
