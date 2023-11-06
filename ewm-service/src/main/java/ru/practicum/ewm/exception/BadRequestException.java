package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.error.ApiError;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private ApiError apiError;

    public BadRequestException(ApiError apiError) {
        this.apiError = apiError;
    }

    public BadRequestException(String message) {
        super(message);
    }
}