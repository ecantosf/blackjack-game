package cat.opteams.blackjack.application.command;

/**
 * Command to delete an existing game.
 *
 * @param gameId the unique identifier of the game to delete (cannot be {@code null} or blank)
 * @see cat.opteams.blackjack.domain.model.aggregate.Game
 */
public record DeleteGameCommand(
        String gameId
) {}
