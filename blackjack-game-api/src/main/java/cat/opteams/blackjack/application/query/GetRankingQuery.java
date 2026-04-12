package cat.opteams.blackjack.application.query;

/**
 * Query to retrieve the player ranking.
 * @param limit maximum number of players to return (default 10)
 */
public record GetRankingQuery(
        Integer limit
) {
    public GetRankingQuery() {
        this(10);
    }
}
