package ru.practicum.ewm.pub;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.PublicEventFindParameters;
import ru.practicum.ewm.event.dto.PublicEventsFindParameters;

import java.util.List;

public interface EventServicePublic {

    List<EventShortDto> getEvents(PublicEventsFindParameters parameters, Pageable pageable);

    EventFullDto getEvent(PublicEventFindParameters parameters);
}