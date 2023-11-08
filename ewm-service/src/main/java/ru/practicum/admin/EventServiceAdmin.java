package ru.practicum.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventsFindParameters;

import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getEvents(EventsFindParameters parameters, Pageable pageable);

    EventFullDto getEvent(Long eventId);
}