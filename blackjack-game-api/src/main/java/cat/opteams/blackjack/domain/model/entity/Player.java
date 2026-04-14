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
        if (id == null) {
            throw new IllegalArgumentException("PlayerId cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("PlayerName cannot be null");
        }
        this.id = id;
        this.name = name;
        this.totalGames = 0;
        this.gamesWon = 0;
        this.totalPoints = 0;
    }

    /**
     * @param id player identifier
     * @param name player name
     * @param totalGames total number of games played
     * @param gamesWon number of games won
     * @param totalPoints total accumulated points
     * @throws IllegalArgumentException if any invariant is violated
     */
    Player(PlayerId id, PlayerName name, int totalGames, int gamesWon, int totalPoints) {
        if (id == null) {
            throw new IllegalArgumentException("PlayerId cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("PlayerName cannot be null");
        }
        if (totalGames < 0) {
            throw new IllegalArgumentException("Total games cannot be negative");
        }
        if (gamesWon < 0) {
            throw new IllegalArgumentException("Games won cannot be negative");
        }
        if (totalPoints < 0) {
            throw new IllegalArgumentException("Total points cannot be negative");
        }
        if (gamesWon > totalGames) {
            throw new IllegalArgumentException("Games won cannot exceed total games");
        }

        this.id = id;
        this.name = name;
        this.totalGames = totalGames;
        this.gamesWon = gamesWon;
        this.totalPoints = totalPoints;
    }

    /**
     * @param id player identifier
     * @param name player name
     * @param totalGames total number of games played
     * @param gamesWon number of games won
     * @param totalPoints total accumulated points
     * @return reconstructed Player
     * @throws IllegalArgumentException if any invariant is violated
     */
    public static Player reconstruct(
            PlayerId id,
            PlayerName name,
            int totalGames,
            int gamesWon,
            int totalPoints
    ) {
        if (id == null) {
            throw new IllegalArgumentException("PlayerId cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("PlayerName cannot be null");
        }
        if (totalGames < 0) {
            throw new IllegalArgumentException("Total games cannot be negative");
        }
        if (gamesWon < 0) {
            throw new IllegalArgumentException("Games won cannot be negative");
        }
        if (totalPoints < 0) {
            throw new IllegalArgumentException("Total points cannot be negative");
        }
        if (gamesWon > totalGames) {
            throw new IllegalArgumentException("Games won cannot exceed total games");
        }

        Player player = new Player(id, name);
        player.totalGames = totalGames;
        player.gamesWon = gamesWon;
        player.totalPoints = totalPoints;
        return player;
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
        if (newName == null) {
            throw new IllegalArgumentException("New name cannot be null");
        }
        this.name = newName;
    }

    public void addWin(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
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

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name=" + name +
                ", totalGames=" + totalGames +
                ", gamesWon=" + gamesWon +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
