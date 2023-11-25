package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationWithoutEvent {
    @NotNull
    @Positive
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotNull
    @NotBlank
    private String title;
}