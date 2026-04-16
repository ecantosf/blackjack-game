package cat.opteams.blackjack.testutil;

import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;

public class PlayerTestBuilder {

    private PlayerId id;
    private PlayerName name;
    private int totalGames;
    private int gamesWon;
    private int totalPoints;

    private PlayerTestBuilder() {
        this.id = new PlayerId();
        this.name = new PlayerName("TestPlayer");
        this.totalGames = 0;
        this.gamesWon = 0;
        this.totalPoints = 0;
    }

    public static PlayerTestBuilder aPlayer() {
        return new PlayerTestBuilder();
    }

    public PlayerTestBuilder withId(PlayerId id) {
        this.id = id;
        return this;
    }

    public PlayerTestBuilder withName(String name) {
        this.name = new PlayerName(name);
        return this;
    }

    public PlayerTestBuilder withStats(int totalGames, int gamesWon, int totalPoints) {
        this.totalGames = totalGames;
        this.gamesWon = gamesWon;
        this.totalPoints = totalPoints;
        return this;
    }

    public PlayerTestBuilder withTotalGames(int totalGames) {
        this.totalGames = totalGames;
        return this;
    }

    public PlayerTestBuilder withGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
        return this;
    }

    public PlayerTestBuilder withTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public Player build() {
        Player player = new Player(id, name);

        for (int i = 0; i < totalGames; i++) {
            boolean won = i < gamesWon;
            int points = won && totalPoints > 0 ? totalPoints / Math.max(gamesWon, 1) : 0;
            if (won) {
                player.addWin(points);
            } else {
                player.addLoss();
            }
        }

        return player;
    }
}
