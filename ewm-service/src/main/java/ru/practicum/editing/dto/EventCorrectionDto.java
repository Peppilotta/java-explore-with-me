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
public class EventCorrectionDto {

    private Long id;

    private String title;

    private String annotation;

    private String description;
}
