package ru.practicum.category.dto;

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
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: name. Error: must not be blank. Value: blank")
    @Size(min = 1, max = 50, message = "Size of name must be between {min} and {max}")
    private String name;
}