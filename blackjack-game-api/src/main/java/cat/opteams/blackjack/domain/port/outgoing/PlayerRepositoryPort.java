package cat.opteams.blackjack.domain.port.outgoing;

import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Output port for reactive persistence of Player entities.
 *
 * <p>This interface defines the contract for reactive CRUD operations
 * on the {@link Player} entity, using Project Reactor for asynchronous
 * and non-blocking processing with R2DBC.</p>
 *
 * <p><b>Hexagonal Architecture:</b> The domain defines this port (abstraction)
 * and the infrastructure layer provides the concrete adapter (MySQL R2DBC).</p>
 *
 * @author Your Name
 * @version 1.0
 * @since 2024-04-11
 */
public interface PlayerRepositoryPort {

    /**
     * Saves a player to the repository reactively.
     *
     * <p>If the player already exists (same {@link PlayerId}), it is updated.
     * Otherwise, a new entry is created.</p>
     *
     * @param player the player to persist (cannot be {@code null})
     * @return {@link Mono} emitting the saved player with updated fields
     * @throws IllegalArgumentException if {@code player} is {@code null}
     */
    Mono<Player> save(Player player);

    /**
     * Finds a player by their unique identifier.
     *
     * @param id the player identifier (cannot be {@code null})
     * @return {@link Mono} emitting the player if found,
     *         or {@link Mono#empty()} if not found
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Mono<Player> findById(PlayerId id);

    /**
     * Finds a player by their name.
     *
     * @param name the player name (cannot be {@code null})
     * @return {@link Mono} emitting the player if found,
     *         or {@link Mono#empty()} if not found
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    Mono<Player> findByName(PlayerName name);

    /**
     * Checks if a player exists with the given name.
     *
     * @param name the player name (cannot be {@code null})
     * @return {@link Mono} emitting {@code true} if a player with the name exists,
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    Mono<Boolean> existsByName(PlayerName name);

    /**
     * Retrieves the player ranking ordered by total points.
     *
     * <p>Players are sorted in descending order by their total points,
     * with the highest-scoring players first.</p>
     *
     * @param limit maximum number of players to return (must be positive)
     * @return {@link Flux} emitting players in descending order of total points,
     *         or {@link Flux#empty()} if no players exist
     * @throws IllegalArgumentException if {@code limit} is less than or equal to zero
     */
    Flux<Player> findAllOrderByTotalPointsDesc(int limit);
}
