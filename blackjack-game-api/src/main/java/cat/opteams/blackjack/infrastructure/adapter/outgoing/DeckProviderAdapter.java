package cat.opteams.blackjack.infrastructure.adapter.outgoing;

import cat.opteams.blackjack.domain.port.outgoing.DeckProviderPort;
import cat.opteams.blackjack.domain.service.Deck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class DeckProviderAdapter implements DeckProviderPort {

    @Override
    public Mono<Deck> getNewDeck() {
        log.debug("Creating new shuffled deck");
        return Mono.fromCallable(() -> {
            Deck deck = new Deck();
            log.debug("Deck created with {} cards", deck.remainingCards());
            return deck;
        });
    }

    @Override
    public Mono<Deck> getNewDeckWithSeed(long seed) {
        log.debug("Creating new shuffled deck with seed: {}", seed);
        return Mono.fromCallable(() -> {
            Deck deck = new Deck(seed);
            log.debug("Deck created with {} cards using seed", deck.remainingCards());
            return deck;
        });
    }
}
