package ru.practicum.event.dto;

import java.util.Objects;

public enum ReviewAction {
    SEND_TO_REVIEW("SEND_TO_REVIEW"),
    CANCEL_REVIEW("CANCEL_REVIEW");

    private final String value;

    ReviewAction(String value) {
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
        for (ReviewAction reviewAction : values()) {
            if (Objects.equals(reviewAction.name(), state)) {
                return true;
            }
        }
        return false;
    }

}