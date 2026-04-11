package cat.opteams.blackjack.domain.port.outgoing;

import cat.opteams.blackjack.domain.service.Deck;
import reactor.core.publisher.Mono;

/**
 * Output port for reactive deck generation.
 *
 * <p>This interface defines the contract for generating card decks in a reactive manner.
 * Although deck generation is inherently synchronous (52 cards, fast operation),
 * it is wrapped in {@link Mono} for reactive consistency across the application.</p>
 *
 * <p><b>Hexagonal Architecture:</b> The domain defines this port (abstraction)
 * and the infrastructure layer provides the concrete adapter for deck creation.</p>
 *
 * @author Your Name
 * @version 1.0
 * @since 2024-04-11
 */
public interface DeckProviderPort {

    /**
     * Generates a new shuffled deck reactively.
     *
     * <p>The deck is randomly shuffled using the default random number generator.
     * Each invocation produces a deck with a different card order.</p>
     *
     * @return {@link Mono} emitting an immutable {@link Deck} instance
     */
    Mono<Deck> getNewDeck();

    /**
     * Generates a new deck with a specific seed for reproducible shuffling.
     *
     * <p>This method is primarily intended for testing purposes, allowing
     * deterministic deck generation by using the same seed value.</p>
     *
     * @param seed the seed for the random number generator
     * @return {@link Mono} emitting an immutable {@link Deck} instance with deterministic order
     */
    Mono<Deck> getNewDeckWithSeed(long seed);
}
