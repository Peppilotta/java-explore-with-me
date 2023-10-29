package ru.practicum.ewm.event.dto;

public enum SortEvent {
    EVENT_DATE("EVENT_DATE"),
    VIEWS("VIEWS");

    private final String value;

    SortEvent(String value) {
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
