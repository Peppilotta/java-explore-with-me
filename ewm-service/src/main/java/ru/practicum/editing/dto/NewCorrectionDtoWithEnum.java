package ru.practicum.editing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCorrectionDtoWithEnum {

    @NotNull
    @NotBlank
    private EventField eventField;

    @NotNull
    @Size(min = 5, max = 7000, message = "Size of content must be between {min} and {max}")
    private String content;
}
