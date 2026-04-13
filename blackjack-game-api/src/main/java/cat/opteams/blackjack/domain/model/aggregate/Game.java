package cat.opteams.blackjack.domain.model.aggregate;

import cat.opteams.blackjack.domain.model.valueobject.*;
import cat.opteams.blackjack.domain.service.DrawResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class Game {
    private final GameId id;
    private final PlayerId playerId;
    private final Hand playerHand;
    private final Hand dealerHand;
    private final Money bet;
    private final GameStatus status;
    private final Player winner;
    private final LocalDateTime createdAt;
    private final LocalDateTime finishedAt;

    public enum Player {
        PLAYER,
        DEALER,
        TIE
    }

    /**
     * Main constructor.
     * To create new games, use startNewGame().
     * This constructor is public ONLY for reconstruction from persistence.
     *
     * @param id game identifier
     * @param playerId player identifier
     * @param bet bet amount
     * @param playerHand player's hand
     * @param dealerHand dealer's hand
     * @param status game status
     * @param winner winner (can be null if not finished)
     * @param createdAt creation date
     * @param finishedAt finish date (can be null)
     * @throws IllegalArgumentException if any required field is null
     */

    public Game(
            GameId id,
            PlayerId playerId,
            Money bet,
            Hand playerHand,
            Hand dealerHand,
            GameStatus status,
            Player winner,
            LocalDateTime createdAt,
            LocalDateTime finishedAt
    ) {
        if (id == null) {
            throw new IllegalArgumentException("GameId cannot be null");
        }
        if (playerId == null) {
            throw new IllegalArgumentException("PlayerId cannot be null");
        }
        if (bet == null) {
            throw new IllegalArgumentException("Bet cannot be null");
        }
        if (playerHand == null) {
            throw new IllegalArgumentException("Player hand cannot be null");
        }
        if (dealerHand == null) {
            throw new IllegalArgumentException("Dealer hand cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        this.id = id;
        this.playerId = playerId;
        this.bet = bet;
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
        this.status = status;
        this.winner = winner;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
    }

    public static Game startNewGame(PlayerId playerId, Money bet) {
        return new Game(
                new GameId(),
                playerId,
                bet,
                new Hand(),
                new Hand(),
                GameStatus.WAITING,
                null,
                LocalDateTime.now(),
                null
        );
    }

    public Game initialize(DrawResult playerCard1, DrawResult playerCard2, DrawResult dealerCard1, DrawResult dealerCard2) {
        if (status != GameStatus.WAITING) {
            throw new IllegalStateException("Game already initialized");
        }

        Hand newPlayerHand = playerHand
                .addCard(playerCard1.card())
                .addCard(playerCard2.card());
        Hand newDealerHand = dealerHand
                .addCard(dealerCard1.card())
                .addCard(dealerCard2.card());

        Game game = new Game(
                id, playerId, bet,
                newPlayerHand, newDealerHand,
                GameStatus.IN_PROGRESS,
                null,
                createdAt,
                null
        );

        if (newPlayerHand.isBlackjack()) {
            return game.stand(List.of());
        }

        return game;
    }

    public Game hit(Card card) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        Hand newPlayerHand = playerHand.addCard(card);
        boolean isBust = newPlayerHand.isBust();

        return new Game(
                id,
                playerId,
                bet,
                newPlayerHand,
                dealerHand,
                isBust ? GameStatus.FINISHED : status,
                isBust ? Player.DEALER : null,
                createdAt,
                isBust ? LocalDateTime.now() : null
        );
    }

    public Game stand(List<Card> dealerCards) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        Hand finalDealerHand = dealerHand;
        for (Card card : dealerCards) {
            finalDealerHand = finalDealerHand.addCard(card);
            if (finalDealerHand.calculateValue() >= 17) {
                break;
            }
        }

        Player winner = determineWinner(playerHand, finalDealerHand);

        return new Game(
                id,
                playerId,
                bet,
                playerHand,
                finalDealerHand,
                GameStatus.FINISHED,
                winner,
                createdAt,
                LocalDateTime.now()
        );
    }

    private Player determineWinner(Hand playerHand, Hand dealerHand) {
        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();

        if (playerHand.isBust()) {
            return Player.DEALER;
        }
        if (dealerHand.isBust()) {
            return Player.PLAYER;
        }
        if (playerValue > dealerValue) {
            return Player.PLAYER;
        }
        if (dealerValue > playerValue) {
            return Player.DEALER;
        }
        return Player.TIE;
    }

    public GameId getId() { return id; }
    public PlayerId getPlayerId() { return playerId; }
    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public Money getBet() { return bet; }
    public GameStatus getStatus() { return status; }
    public Player getWinner() { return winner; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }

    public Card getDealerVisibleCard() {
        if (dealerHand.getCards().isEmpty()) {
            return null;
        }
        return dealerHand.getCards().get(0);
    }

    public boolean isFinished() {
        return status == GameStatus.FINISHED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", status=" + status +
                ", winner=" + winner +
                '}';
    }
}
