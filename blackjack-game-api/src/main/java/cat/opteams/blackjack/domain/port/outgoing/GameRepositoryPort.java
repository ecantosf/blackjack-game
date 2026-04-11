package cat.opteams.blackjack.domain.port.outgoing;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Output port for reactive persistence of Blackjack games.
 *
 * <p>This interface defines the contract for reactive CRUD operations
 * on the {@link Game} aggregate, using Project Reactor for asynchronous
 * and non-blocking processing.</p>
 *
 * <p><b>Hexagonal Architecture:</b> The domain defines this port (abstraction)
 * and the infrastructure layer provides the concrete adapter (MongoDB Reactive).</p>
 *
 * @author Your Name
 * @version 1.0
 * @since 2024-04-11
 */
public interface GameRepositoryPort {

    /**
     * Saves a game to the repository reactively.
     *
     * <p>If the game already exists (same {@link GameId}), it is updated.
     * Otherwise, a new entry is created.</p>
     *
     * @param game the game to persist (cannot be {@code null})
     * @return {@link Mono} emitting the saved game with updated fields
     * @throws IllegalArgumentException if {@code game} is {@code null}
     */
    Mono<Game> save(Game game);

    /**
     * Finds a game by its unique identifier.
     *
     * @param id the game identifier (cannot be {@code null})
     * @return {@link Mono} emitting the game if found,
     *         or {@link Mono#empty()} if not found
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Mono<Game> findById(GameId id);

    /**
     * Checks if a game exists with the given identifier.
     *
     * @param id the game identifier (cannot be {@code null})
     * @return {@link Mono} emitting {@code true} if the game exists,
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Mono<Boolean> existsById(GameId id);

    /**
     * Deletes a game from the repository.
     *
     * <p>If the game does not exist, the operation completes without error.</p>
     *
     * @param id the identifier of the game to delete (cannot be {@code null})
     * @return {@link Mono} that completes when the operation finishes
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Mono<Void> deleteById(GameId id);

    /**
     * Retrieves all games associated with a player.
     *
     * @param playerId the player identifier (cannot be {@code null})
     * @return {@link Flux} emitting the player's games in chronological order,
     *         or {@link Flux#empty()} if the player has no games
     * @throws IllegalArgumentException if {@code playerId} is {@code null}
     */
    Flux<Game> findAllByPlayerId(PlayerId playerId);
}
