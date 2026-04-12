package cat.opteams.blackjack.application.mapper;

public record RankingEntryResponse(
        String playerId,
        String playerName,
        int totalGames,
        int gamesWon,
        int totalPoints,
        double winRate
) {}
