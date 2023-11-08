package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateCompilationRequest {

    @Singular
    private List<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String title;
}