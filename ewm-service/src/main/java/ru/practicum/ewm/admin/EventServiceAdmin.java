package ru.practicum.ewm.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getEvents(List<Long> users,
                                 List<String> states,
                                 List<String> categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 Pageable pageable);

    EventFullDto getEvent(Long eventId);
}