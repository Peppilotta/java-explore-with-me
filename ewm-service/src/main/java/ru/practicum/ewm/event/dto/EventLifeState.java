package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public enum EventLifeState {
    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    CANCELED("CANCELED");

    private final String value;

    EventLifeState(String value) {
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