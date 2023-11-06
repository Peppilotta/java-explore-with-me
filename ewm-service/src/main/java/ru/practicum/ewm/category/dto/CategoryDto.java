package ru.practicum.ewm.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CategoryDto {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String name;
}