package ru.practicum.event.dto;

import java.util.Objects;

public enum StateAction {
    PUBLISH_EVENT("PUBLISH_EVENT"),
    REJECT_EVENT("REJECT_EVENT");

    private final String value;

    StateAction(String value) {
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
        for (StateAction stateAction : values()) {
            if (Objects.equals(stateAction.name(), state)) {
                return true;
            }
        }
        return false;
    }
}