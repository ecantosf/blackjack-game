package cat.opteams.blackjack.application.command;

/**
 * Command to update a player's name.
 *
 * @param playerId the unique identifier of the player (cannot be {@code null} or blank)
 * @param newName the new name for the player (cannot be {@code null}, blank, or already in use)
 *
 * @see cat.opteams.blackjack.domain.model.entity.Player
 * @see cat.opteams.blackjack.domain.model.valueobject.PlayerName
 */
public record UpdatePlayerNameCommand(
        String playerId,
        String newName
) {}
