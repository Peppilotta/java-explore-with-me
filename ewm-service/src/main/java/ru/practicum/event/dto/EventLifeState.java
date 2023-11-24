package ru.practicum.event.dto;

import java.util.Objects;

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

    public static boolean existsByName(String state) {
        for (EventLifeState lifeState : values()) {
            if (Objects.equals(lifeState.name(), state)) {
                return true;
            }
        }
        return false;
    }
}