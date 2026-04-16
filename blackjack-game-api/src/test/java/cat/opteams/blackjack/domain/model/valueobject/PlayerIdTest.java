package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlayerId Value Object Tests")
class PlayerIdTest extends UnitTest {

    @Test
    @DisplayName("Should create PlayerId with UUID")
    void shouldCreatePlayerIdWithUUID() {
        UUID uuid = UUID.randomUUID();
        PlayerId playerId = new PlayerId(uuid);

        assertEquals(uuid, playerId.getValue());
    }

    @Test
    @DisplayName("Should throw exception when UUID is null")
    void shouldThrowExceptionWhenUuidIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PlayerId((UUID) null));
    }

    @Test
    @DisplayName("Should generate new PlayerId")
    void shouldGenerateNewPlayerId() {
        PlayerId playerId = new PlayerId();
        assertNotNull(playerId);
        assertNotNull(playerId.getValue());
    }

    @Test
    @DisplayName("Should create PlayerId from string")
    void shouldCreatePlayerIdFromString() {
        String uuidString = "987e6543-e21b-12d3-a456-426614174000";
        PlayerId playerId = new PlayerId(uuidString);

        assertEquals(uuidString, playerId.getValue().toString());
    }

    @Test
    @DisplayName("Should be equal when UUIDs are the same")
    void shouldBeEqualWhenUuidsAreSame() {
        UUID uuid = UUID.randomUUID();
        PlayerId playerId1 = new PlayerId(uuid);
        PlayerId playerId2 = new PlayerId(uuid);

        assertEquals(playerId1, playerId2);
        assertEquals(playerId1.hashCode(), playerId2.hashCode());
    }
}
