package ru.practicum.services.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicEventFindParameters;
import ru.practicum.event.dto.PublicEventsFindParameters;

import java.util.List;

public interface EventServicePublic {

    List<EventShortDto> getEvents(PublicEventsFindParameters parameters, Pageable pageable);

    EventFullDto getEvent(PublicEventFindParameters parameters);
}