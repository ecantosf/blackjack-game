package cat.opteams.blackjack.application.command;

/**
 * Command to perform an action in an existing Blackjack game.
 *
 * @param gameId the unique identifier of the game (cannot be {@code null} or blank)
 * @param action the action to perform (HIT or STAND, cannot be {@code null})
 *
 * @see cat.opteams.blackjack.domain.model.aggregate.Game
 * @see Action
 */
public record PlayCommand(
        String gameId,
        Action action
) {
    public enum Action {
        /**
         * Request an additional card from the deck.
         */
        HIT,

        /**
         * End the player's turn and let the dealer play.
         */
        STAND
    }
}
