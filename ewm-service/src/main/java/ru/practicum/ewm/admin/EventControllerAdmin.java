package ru.practicum.ewm.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
public class EventControllerAdmin {

    private static final String EVENT_ID_FIELD_NAME = "id";

    private final EventServiceAdmin service;

    public EventControllerAdmin(EventServiceAdmin service) {
        this.service = service;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<String> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, EVENT_ID_FIELD_NAME));
        return service.getEvents(users, states, categories, rangeStart, rangeEnd, pageable);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") @Positive Long eventId) {
        return service.getEvent(eventId);
    }
}