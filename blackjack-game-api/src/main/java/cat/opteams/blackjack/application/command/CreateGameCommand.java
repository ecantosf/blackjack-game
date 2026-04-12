package cat.opteams.blackjack.application.command;

import java.math.BigDecimal;

/**
 * Command to create a new Blackjack game.
 *
 * @param playerName the name of the player (cannot be {@code null} or blank)
 * @param bet the bet amount (must be positive and within allowed limits)
 *
 * @see cat.opteams.blackjack.domain.model.valueobject.PlayerName
 * @see cat.opteams.blackjack.domain.model.valueobject.Money
 */
public record CreateGameCommand(
        String playerName,
        BigDecimal bet
) {}
