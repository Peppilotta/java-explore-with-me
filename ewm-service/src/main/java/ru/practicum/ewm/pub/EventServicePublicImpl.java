package ru.practicum.ewm.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventServicePublicImpl implements EventServicePublic {

    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Pageable pageable) {
        return new ArrayList<>();
    }

    public EventFullDto getEvent(Long id) {
        return new EventFullDto();
    }
}