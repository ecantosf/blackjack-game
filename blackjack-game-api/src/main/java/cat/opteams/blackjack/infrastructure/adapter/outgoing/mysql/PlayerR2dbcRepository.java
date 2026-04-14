package cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerR2dbcRepository extends R2dbcRepository<PlayerEntity, String> {

    /**
     * Finds a player by their name.
     * @param name player name
     * @return Mono with the player (can be empty)
     */
    Mono<PlayerEntity> findByName(String name);

    /**
     * Verifies if a player exists with the given name.
     * @param name player name
     * @return Mono<Boolean> with true if exists
     */
    Mono<Boolean> existsByName(String name);

    /**
     * Gets all players ordered by total points (descending).
     * @param limit maximum number of players to return
     * @return Flux of ordered PlayerEntity
     */
    @Query("SELECT * FROM players ORDER BY total_points DESC LIMIT :limit")
    Flux<PlayerEntity> findAllOrderByTotalPointsDesc(int limit);
}
