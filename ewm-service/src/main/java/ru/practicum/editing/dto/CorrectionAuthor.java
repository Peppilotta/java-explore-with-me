package ru.practicum.editing.dto;

public enum CorrectionAuthor {
    ADMIN("ADMIN"),
    ADMIN_ONLY_NOTE("ADMIN_ONLY_NOTE"),
    USER("USER");
    private final String value;

    CorrectionAuthor(String value) {
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
