package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.error.ApiError;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private final ApiError apiError;

    public BadRequestException(ApiError apiError) {
        this.apiError = apiError;
    }
}