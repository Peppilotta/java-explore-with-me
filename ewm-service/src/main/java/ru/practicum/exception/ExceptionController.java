package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.ApiError;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleNotFound(final NotFoundException e) {
        log.error(e.getApiError().getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getApiError());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(final BadRequestException e) {
        log.warn(e.getApiError().getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getApiError());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(final ConflictException e) {
        log.warn(e.getApiError().getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getApiError());
    }
}