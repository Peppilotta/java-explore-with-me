package ru.practicum.editing.dto;

import java.util.Objects;

public enum RevisionState {

    INITIAL(""),
    EDITED(""),
    REPEATED(""),
    RESOLVED("");

    private final String value;

    RevisionState(String value) {
        this.value = value;
    }

    public static boolean existsByName(String sort) {
        for (RevisionState revisionState : values()) {
            if (Objects.equals(revisionState.name(), sort)) {
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