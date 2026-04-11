package cat.opteams.blackjack.domain.model.valueobject;

import java.util.Objects;

public final class PlayerName {
    private final String value;

    public PlayerName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be null or blank");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Player name cannot exceed 50 characters");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerName that = (PlayerName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
