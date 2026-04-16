package cat.opteams.blackjack.domain.model.entity;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Entity Tests")
class PlayerTest extends UnitTest {

    @Test
    @DisplayName("Should create player with name")
    void shouldCreatePlayerWithName() {
        PlayerName name = new PlayerName("Joan");
        Player player = new Player(new PlayerId(), name);

        assertNotNull(player);
        assertNotNull(player.getId());
        assertEquals(name, player.getName());
        assertEquals(0, player.getTotalGames());
        assertEquals(0, player.getGamesWon());
        assertEquals(0, player.getTotalPoints());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Player(new PlayerId(), null)
        );
    }

    @Test
    @DisplayName("Should record game played and won")
    void shouldRecordGamePlayedAndWon() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));
        int points = 100;

        player.addWin(points);

        assertEquals(1, player.getTotalGames());
        assertEquals(1, player.getGamesWon());
        assertEquals(points, player.getTotalPoints());
    }

    @Test
    @DisplayName("Should record game played and lost")
    void shouldRecordGamePlayedAndLost() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));

        player.addLoss();

        assertEquals(1, player.getTotalGames());
        assertEquals(0, player.getGamesWon());
        assertEquals(0, player.getTotalPoints());
    }

    @Test
    @DisplayName("Should calculate win rate correctly")
    void shouldCalculateWinRateCorrectly() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));
        player.addWin(100);
        player.addWin(100);
        player.addLoss();
        player.addLoss();

        double winRate = player.getWinRate();

        assertEquals(0.5, winRate, 0.01);
    }

    @Test
    @DisplayName("Should return 0 win rate when no games played")
    void shouldReturnZeroWinRateWhenNoGamesPlayed() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));

        assertEquals(0.0, player.getWinRate());
    }

    @Test
    @DisplayName("Should accumulate points correctly")
    void shouldAccumulatePointsCorrectly() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));

        player.addWin(100);
        player.addWin(150);
        player.addWin(200);

        assertEquals(450, player.getTotalPoints());
    }

    @Test
    @DisplayName("Should update name correctly")
    void shouldUpdateNameCorrectly() {
        Player player = new Player(new PlayerId(), new PlayerName("Joan"));
        PlayerName newName = new PlayerName("Joan Marti");

        player.updateName(newName);

        assertEquals(newName, player.getName());
    }

    @Test
    @DisplayName("Should be equal when IDs are the same")
    void shouldBeEqualWhenIdsAreSame() {
        PlayerId id = new PlayerId();
        PlayerName name = new PlayerName("Joan");
        Player player1 = new Player(id, name);
        Player player2 = new Player(id, name);

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when IDs are different")
    void shouldNotBeEqualWhenIdsAreDifferent() {
        PlayerName name = new PlayerName("Joan");
        Player player1 = new Player(new PlayerId(), name);
        Player player2 = new Player(new PlayerId(), name);

        assertNotEquals(player1, player2);
    }
}
