package cat.opteams.blackjack.domain.model.entity;

import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;

import java.util.Objects;

public class Player {
    private final PlayerId id;
    private PlayerName name;
    private int totalGames;
    private int gamesWon;
    private int totalPoints;

    public Player(PlayerId id, PlayerName name) {
        this.id = id;
        this.name = name;
        this.totalGames = 0;
        this.gamesWon = 0;
        this.totalPoints = 0;
    }

    public PlayerId getId() {
        return id;
    }

    public PlayerName getName() {
        return name;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void updateName(PlayerName newName) {
        this.name = newName;
    }

    public void addWin(int points) {
        this.totalGames++;
        this.gamesWon++;
        this.totalPoints += points;
    }

    public void addLoss() {
        this.totalGames++;
    }

    public double getWinRate() {
        if (totalGames == 0) {
            return 0.0;
        }
        return (double) gamesWon / totalGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
