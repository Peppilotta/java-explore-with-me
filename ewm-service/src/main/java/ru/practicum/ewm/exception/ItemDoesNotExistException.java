package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.error.ApiError;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemDoesNotExistException extends RuntimeException {

    public ItemDoesNotExistException(String message) {
        super(message);
    }
}