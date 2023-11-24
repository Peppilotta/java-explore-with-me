package ru.practicum.editing.dto;

import java.util.Objects;

public enum CorrectionAuthor {
    ADMIN("ADMIN"),
    ADMIN_ONLY_NOTE("ADMIN_ONLY_NOTE"),
    USER("USER");
    private final String value;

    CorrectionAuthor(String value) {
        this.value = value;
    }

    public static boolean existsByName(String sort) {
        for (CorrectionAuthor correctionAuthor : values()) {
            if (Objects.equals(correctionAuthor.name(), sort)) {
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
