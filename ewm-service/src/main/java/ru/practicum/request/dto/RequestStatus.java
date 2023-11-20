package ru.practicum.request.dto;

import java.util.Objects;

public enum RequestStatus {
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED"),
    PENDING("PENDING"),
    CANCELED("CANCELED");

    private final String value;

    RequestStatus(String value) {

        this.value = value;
    }

    public static boolean existsByName(String state) {
        for (RequestStatus requestStatus : values()) {
            if (Objects.equals(requestStatus.name(), state)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {

        return value;
    }

    @Override
    public String toString() {

        return String.valueOf(value);
    }
}