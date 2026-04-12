package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.application.command.CreateGameCommand;
import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.validator.CreateGameCommandValidator;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import cat.opteams.blackjack.domain.port.outgoing.DeckProviderPort;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import cat.opteams.blackjack.domain.service.Deck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateGameHandler {

    private final CreateGameCommandValidator validator;
    private final PlayerRepositoryPort playerRepository;
    private final GameRepositoryPort gameRepository;
    private final DeckProviderPort deckProvider;
    private final GameResponseMapper responseMapper;

    public Mono<GameResponse> handle(CreateGameCommand command) {
        log.debug("Handling CreateGameCommand for player: {}", command.playerName());

        validator.validate(command);

        return findOrCreatePlayer(command.playerName())
                .flatMap(player -> deckProvider.getNewDeck()
                        .map(deck -> new GameContext(player, deck)))
                .flatMap(context -> initializeGame(context.player, context.deck, command.bet()))
                .flatMap(game -> gameRepository.save(game)
                        .map(savedGame -> responseMapper.toResponse(savedGame, command.playerName())))
                .doOnSuccess(response -> log.info("Game created successfully: {}", response.id()))
                .doOnError(error -> log.error("Error creating game: {}", error.getMessage()));
    }

    private Mono<Player> findOrCreatePlayer(String playerName) {
        PlayerName name = new PlayerName(playerName);

        return playerRepository.findByName(name)
                .switchIfEmpty(Mono.defer(() -> {
                    Player newPlayer = new Player(new PlayerId(), name);
                    return playerRepository.save(newPlayer);
                }));
    }

    private Mono<Game> initializeGame(Player player, Deck deck, BigDecimal betAmount) {
        Money bet = new Money(betAmount);
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();

        Game initialized = game.initialize(draw1, draw2, draw3, draw4);
        return Mono.just(initialized);
    }

    private record GameContext(Player player, Deck deck) {}
}
