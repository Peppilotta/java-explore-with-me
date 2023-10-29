package ru.practicum.ewm.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {

    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<String> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Pageable pageable) {
        return new ArrayList<>();
    }

    public EventFullDto getEvent(Long eventId) {
        return new EventFullDto();
    }
}