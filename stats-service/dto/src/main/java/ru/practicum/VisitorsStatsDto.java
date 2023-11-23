package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VisitorsStatsDto {

    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    private Long hits;
}