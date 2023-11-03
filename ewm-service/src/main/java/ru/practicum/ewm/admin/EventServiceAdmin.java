package ru.practicum.ewm.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFindParameters;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getEvents(EventFindParameters parameters, Pageable pageable);

    EventFullDto getEvent(Long eventId);
}