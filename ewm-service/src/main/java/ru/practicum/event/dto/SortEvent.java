package ru.practicum.event.dto;

import java.util.Objects;

public enum SortEvent {
    EVENT_DATE("EVENT_DATE"),
    VIEWS("VIEWS");

    private final String value;

    SortEvent(String value) {
        this.value = value;
    }

    public static boolean existsByName(String sort) {
        for (SortEvent sortEvent : values()) {
            if (Objects.equals(sortEvent.name(), sort)) {
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