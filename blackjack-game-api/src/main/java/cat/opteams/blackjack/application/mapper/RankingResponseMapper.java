package cat.opteams.blackjack.application.mapper;

import cat.opteams.blackjack.domain.model.entity.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RankingResponseMapper {

    public List<RankingEntryResponse> toResponseList(List<Player> players) {
        return players.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RankingEntryResponse toResponse(Player player) {
        return new RankingEntryResponse(
                player.getId().getValue().toString(),
                player.getName().getValue(),
                player.getTotalGames(),
                player.getGamesWon(),
                player.getTotalPoints(),
                player.getWinRate()
        );
    }
}
