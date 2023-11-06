package ru.practicum.ewm.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventsFindParameters;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getEvents(EventsFindParameters parameters, Pageable pageable);

    EventFullDto getEvent(Long eventId);
}