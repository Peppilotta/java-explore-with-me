package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewUserRequest {

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @Size(min = 2, max = 250, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String name;

    @NotNull(message = "Field: email. Error: must not be blank. Value: null")
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String email;
}