package ru.practicum.event.dto;

public enum EventLifeState {
    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    NOTED("NOTED"),
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