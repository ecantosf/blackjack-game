package cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql;

import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlayerEntityMapper {

    /**
     * Converts a Player (domain) to PlayerEntity (persistence).
     * @param player the domain entity
     * @return entity for MySQL, or null if player is null
     */
    public PlayerEntity toEntity(Player player) {
        if (player == null) {
            log.warn("Attempted to convert null Player to entity");
            return null;
        }

        return PlayerEntity.builder()
                .id(player.getId().getValue().toString())
                .name(player.getName().getValue())
                .totalGames(player.getTotalGames())
                .gamesWon(player.getGamesWon())
                .totalPoints(player.getTotalPoints())
                .build();
    }

    /**
     * Converts a PlayerEntity (persistence) to Player (domain).
     * @param entity MySQL entity
     * @return domain entity, or null if entity is null
     */
    public Player toDomain(PlayerEntity entity) {
        if (entity == null) {
            log.warn("Attempted to convert null PlayerEntity to domain");
            return null;
        }

        if (entity.getId() == null || entity.getId().isBlank()) {
            throw new IllegalArgumentException("PlayerEntity has null or blank id");
        }
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("PlayerEntity has null or blank name");
        }

        PlayerId playerId = new PlayerId(entity.getId());
        PlayerName playerName = new PlayerName(entity.getName());

        return Player.reconstruct(
                playerId,
                playerName,
                entity.getTotalGames(),
                entity.getGamesWon(),
                entity.getTotalPoints()
        );
    }
}
