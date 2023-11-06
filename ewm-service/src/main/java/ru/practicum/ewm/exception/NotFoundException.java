package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.error.ApiError;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private ApiError apiError;

    public NotFoundException(ApiError apiError) {
        this.apiError = apiError;
    }

    public NotFoundException(String message) {
        super(message);
    }
}