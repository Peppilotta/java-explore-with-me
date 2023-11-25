package ru.practicum.editing.controller;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.editing.service.CorrectionService;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users/{userId}/corrections/{eventId}")
@Validated
@RequiredArgsConstructor
public class CorrectionControllerUser {

    private final CorrectionService correctionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CorrectionDto> getCorrectionForEvent(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId,
                                                     @RequestParam(required = false) List<EventField> eventFields,
                                                     @RequestParam(required = false) List<RevisionState> revisionStates) {
        return correctionService.getCorrectionForEvent(userId, eventId,
                Objects.isNull(eventFields) ? new ArrayList<>() : eventFields,
                Objects.isNull(revisionStates) ? new ArrayList<>() : revisionStates);
    }
}