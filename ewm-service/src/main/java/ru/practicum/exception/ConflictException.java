package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.error.ApiError;

@Getter
@Setter
public class ConflictException extends RuntimeException {

    private final ApiError apiError;

    public ConflictException(ApiError apiError) {
        this.apiError = apiError;
    }
}