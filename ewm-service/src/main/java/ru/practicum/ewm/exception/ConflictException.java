package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.error.ApiError;

@Getter
@Setter
public class ConflictException extends RuntimeException {

    private ApiError apiError;

    public ConflictException(ApiError apiError) {
        this.apiError = apiError;
    }

    public ConflictException(String message) {
        super(message);
    }
}
