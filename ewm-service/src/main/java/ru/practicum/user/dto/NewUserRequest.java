package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: name. Error: must not be blank. Value: empty")
    @Size(min = 2, max = 250, message = "Size of name must be between {min} and {max}")
    private String name;

    @NotNull(message = "Field: email. Error: must not be blank. Value: null")
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "Size of email must be between {min} and {max}")
    private String email;
}