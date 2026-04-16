package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameId Value Object Tests")
class GameIdTest extends UnitTest {

    @Test
    @DisplayName("Should create GameId with UUID")
    void shouldCreateGameIdWithUUID() {
        UUID uuid = UUID.randomUUID();
        GameId gameId = new GameId(uuid);

        assertEquals(uuid, gameId.getValue());
    }

    @Test
    @DisplayName("Should throw exception when UUID is null")
    void shouldThrowExceptionWhenUuidIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new GameId((UUID) null));
    }

    @Test
    @DisplayName("Should generate new GameId")
    void shouldGenerateNewGameId() {
        GameId gameId = new GameId();
        assertNotNull(gameId);
        assertNotNull(gameId.getValue());
    }

    @Test
    @DisplayName("Should create GameId from string")
    void shouldCreateGameIdFromString() {
        String uuidString = "123e4567-e89b-12d3-a456-426614174000";
        GameId gameId = new GameId(uuidString);

        assertEquals(uuidString, gameId.getValue().toString());
    }

    @Test
    @DisplayName("Should be equal when UUIDs are the same")
    void shouldBeEqualWhenUuidsAreSame() {
        UUID uuid = UUID.randomUUID();
        GameId gameId1 = new GameId(uuid);
        GameId gameId2 = new GameId(uuid);

        assertEquals(gameId1, gameId2);
        assertEquals(gameId1.hashCode(), gameId2.hashCode());
    }
}
