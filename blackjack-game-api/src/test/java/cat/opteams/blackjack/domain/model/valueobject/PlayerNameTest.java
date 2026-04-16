package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlayerName Value Object Tests")
class PlayerNameTest extends UnitTest {

    @Test
    @DisplayName("Should create player name with valid value")
    void shouldCreatePlayerNameWithValidValue() {
        PlayerName name = new PlayerName("Joan");
        assertEquals("Joan", name.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when name is null or empty")
    void shouldThrowExceptionWhenNameIsNullOrEmpty(String value) {
        assertThrows(IllegalArgumentException.class, () -> new PlayerName(value));
    }

    @Test
    @DisplayName("Should trim whitespace from name")
    void shouldTrimWhitespaceFromName() {
        PlayerName name = new PlayerName("  Joan  ");
        assertEquals("Joan", name.getValue());
    }

    @Test
    @DisplayName("Should accept short names (minimum 1 character)")
    void shouldAcceptShortNames() {
        // La validació real només requereix que no estigui buit
        assertDoesNotThrow(() -> new PlayerName("J"));
        PlayerName name = new PlayerName("J");
        assertEquals("J", name.getValue());
    }

    @Test
    @DisplayName("Should throw exception when name is too long (over 50 characters)")
    void shouldThrowExceptionWhenNameIsTooLong() {
        String longName = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> new PlayerName(longName));
    }

    @Test
    @DisplayName("Should accept name at maximum length (50 characters)")
    void shouldAcceptNameAtMaximumLength() {
        String maxName = "A".repeat(50);
        assertDoesNotThrow(() -> new PlayerName(maxName));
        PlayerName name = new PlayerName(maxName);
        assertEquals(maxName, name.getValue());
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreSame() {
        PlayerName name1 = new PlayerName("Joan");
        PlayerName name2 = new PlayerName("Joan");

        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        PlayerName name1 = new PlayerName("Joan");
        PlayerName name2 = new PlayerName("Maria");

        assertNotEquals(name1, name2);
    }
}
