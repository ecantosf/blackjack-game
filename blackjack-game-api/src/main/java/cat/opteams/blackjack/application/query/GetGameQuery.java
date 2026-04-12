package cat.opteams.blackjack.application.query;

/**
 * Query to retrieve the details of a game.
 * @param gameId game identifier
 */
public record GetGameQuery(
        String gameId
) {}
