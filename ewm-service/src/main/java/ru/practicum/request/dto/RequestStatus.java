package ru.practicum.request.dto;

public enum RequestStatus {
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED"),
    PENDING("PENDING"),
    CANCELED("CANCELED");

    private final String value;

    RequestStatus(String value) {

        this.value = value;
    }

    public String getValue() {

        return value;
    }

    @Override
    public String toString() {

        return String.valueOf(value);
    }
}