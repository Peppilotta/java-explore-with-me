package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.error.ApiError;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private final ApiError apiError;

    public NotFoundException(ApiError apiError) {
        this.apiError = apiError;
    }
}