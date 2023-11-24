package ru.practicum.editing.dto;

import java.util.Objects;

public enum EventField {
    ANNOTATION("ANNOTATION"),
    DESCRIPTION ("DESCRIPTION"),
    CATEGORY("CATEGORY"),
    EVENT_DATE("EVENT_DATE"),
    LOCATION("LOCATION"),
    PAID("PAID"),
    PARTICIPANT_LIMIT("PARTICIPANT_LIMIT"),
    REQUEST_MODERATION("REQUEST_MODERATION"),
    TITLE("TITLE");

    private final String value;

    EventField(String value) {
        this.value = value;
    }

    public static boolean existsByName(String sort) {
        for (EventField eventField : values()) {
            if (Objects.equals(eventField.name(), sort)) {
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
