package cat.opteams.blackjack.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class GameId {
    private final UUID value;

    public GameId() {
        this.value = UUID.randomUUID();
    }

    public GameId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("GameId cannot be null");
        }
        this.value = value;
    }

    public GameId(String value) {
        this(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId = (GameId) o;
        return Objects.equals(value, gameId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
