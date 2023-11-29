package ru.practicum.editing.dto;

public enum RevisionState {

    INITIAL(""),
    EDITED(""),
    REPEATED(""),
    RESOLVED("");

    private final String value;

    RevisionState(String value) {
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