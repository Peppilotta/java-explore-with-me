package ru.practicum.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.error.ApiError;

@Getter
@Setter
@NoArgsConstructor
public class BadRequestException extends RuntimeException {

    private ApiError apiError;

    public BadRequestException(ApiError apiError) {
        this.apiError = apiError;
    }
}