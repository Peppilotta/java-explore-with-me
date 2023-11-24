package ru.practicum.editing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.editing.dto.CorrectionDto;
import ru.practicum.editing.dto.EventField;
import ru.practicum.event.dto.ReviewAction;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/corrections/{eventId}")
@Validated
public class CorrectionControllerUser {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CorrectionDto> getCorrectionForEvent(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId,
                                                     @RequestParam(required = false) List<EventField> eventFields,
                                                     @RequestParam(required = false) List<ReviewAction> reviewActions) {
        return new ArrayList<>();
    }
}
