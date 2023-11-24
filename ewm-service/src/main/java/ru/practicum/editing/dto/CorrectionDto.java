package ru.practicum.editing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionDto {

    private Long id;

    private EventCorrectionDto event;

    private EventField eventField;

    private String adminNote;

    private String before;

    private String after;

    private CorrectionAuthor correctionAuthor;

    private RevisionState state;
}