package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Boolean pinned = false;

    @NotNull(message = "Field: title. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: title. Error: must not be blank. Value: blank")
    @Size(min = 1, max = 50, message = "Size of title must be between {min} and {max}")
    private String title;

    @Singular
    private List<Long> events;
}