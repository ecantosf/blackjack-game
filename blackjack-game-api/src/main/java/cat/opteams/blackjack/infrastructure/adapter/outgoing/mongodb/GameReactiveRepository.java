package cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameReactiveRepository extends ReactiveMongoRepository<GameDocument, String> {

    /**
     * Finds all games for a player.
     * @param playerId player identifier
     * @return Flux of GameDocument
     */
    Flux<GameDocument> findByPlayerId(String playerId);
}
