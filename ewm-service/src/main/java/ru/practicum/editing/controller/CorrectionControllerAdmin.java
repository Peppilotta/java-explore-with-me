package ru.practicum.editing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.editing.dto.CorrectionDto;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.NewCorrectionDtos;
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.editing.service.CorrectionService;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/corrections/{eventId}")
@Validated
@RequiredArgsConstructor
public class CorrectionControllerAdmin {

    private final CorrectionService correctionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CorrectionDto> getCorrectionForEventAdmin(@PathVariable @Positive Long eventId,
                                                          @RequestParam(required = false) List<EventField> eventFields,
                                                          @RequestParam(required = false) List<RevisionState> revisionStates) {
        return new ArrayList<>();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<CorrectionDto> postNotesByAdmin(@PathVariable @Positive Long eventId,
                                                @RequestBody NewCorrectionDtos correctionDtos) {

        return correctionService.postNotesByAdmin(eventId, correctionDtos);
    }

    @PatchMapping("/approve")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CorrectionDto> reviewCorrectionByAdmin(@PathVariable @Positive Long eventId,
                                                       @RequestParam List<EventField> eventFields) {

        return new ArrayList<>();
    }
}
