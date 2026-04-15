package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.application.command.PlayCommand;
import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.validator.PlayCommandValidator;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.Card;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;  // ← IMPORT AFEGIT
import cat.opteams.blackjack.domain.port.outgoing.DeckProviderPort;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import cat.opteams.blackjack.shared.exception.GameAlreadyFinishedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayHandler {

    private final PlayCommandValidator validator;
    private final GameRepositoryPort gameRepository;
    private final PlayerRepositoryPort playerRepository;
    private final DeckProviderPort deckProvider;
    private final GameResponseMapper responseMapper;

    public Mono<GameResponse> handle(PlayCommand command) {
        log.debug("Handling PlayCommand for game: {}, action: {}", command.gameId(), command.action());

        validator.validate(command);

        GameId gameId = new GameId(command.gameId());

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Game not found: " + command.gameId())))
                .flatMap(game -> executeAction(game, command.action())
                        .flatMap(updatedGame -> saveGameAndUpdatePlayer(updatedGame, command)))
                .doOnSuccess(response -> log.info("Play executed successfully for game: {}", response.id()))
                .doOnError(error -> log.error("Error executing play: {}", error.getMessage()));
    }

    private Mono<Game> executeAction(Game game, PlayCommand.Action action) {
        return switch (action) {
            case HIT -> handleHit(game);
            case STAND -> handleStand(game);
        };
    }

    private Mono<Game> handleHit(Game game) {
        if (game.isFinished()) {
            return Mono.error(new IllegalStateException("Game is already finished"));
        }

        return deckProvider.getNewDeck()
                .map(deck -> {
                    var draw = deck.draw();
                    return game.hit(draw.card());
                });
    }

    private Mono<Game> handleStand(Game game) {
        if (game.isFinished()) {
            return Mono.error(new GameAlreadyFinishedException("Game is already finished"));
        }

        return deckProvider.getNewDeck()
                .map(deck -> {
                    List<Card> dealerCards = new ArrayList<>();
                    var currentDeck = deck;
                    var currentHand = game.getDealerHand();

                    while (currentHand.calculateValue() < 17) {
                        var draw = currentDeck.draw();
                        dealerCards.add(draw.card());
                        currentHand = currentHand.addCard(draw.card());
                        currentDeck = draw.remainingDeck();
                    }

                    return game.stand(dealerCards);
                });
    }

    private Mono<GameResponse> saveGameAndUpdatePlayer(Game game, PlayCommand command) {
        return gameRepository.save(game)
                .flatMap(savedGame -> {
                    if (!savedGame.isFinished()) {
                        return getPlayerName(savedGame.getPlayerId())
                                .map(playerName -> responseMapper.toResponse(savedGame, playerName));
                    }

                    return updatePlayerStats(savedGame)
                            .then(getPlayerName(savedGame.getPlayerId()))
                            .map(playerName -> responseMapper.toResponse(savedGame, playerName));
                });
    }

    private Mono<Void> updatePlayerStats(Game game) {
        return playerRepository.findById(game.getPlayerId())
                .flatMap(player -> {
                    int betAmount = game.getBet().getAmount().intValue();

                    if (game.getWinner() == Game.Player.PLAYER) {
                        log.debug("Player won! Adding win and {} points", betAmount);
                        player.addWin(betAmount);
                    } else if (game.getWinner() == Game.Player.DEALER) {
                        log.debug("Player lost! Adding loss");
                        player.addLoss();
                    } else {
                        log.debug("Game was a tie! No stats update needed");
                        return Mono.empty();
                    }

                    return playerRepository.save(player).then();
                })
                .doOnSuccess(v -> log.debug("Player stats updated successfully for game: {}", game.getId().getValue()))
                .doOnError(error -> log.error("Error updating player stats: {}", error.getMessage()));
    }

    private Mono<String> getPlayerName(PlayerId playerId) {
        return playerRepository.findById(playerId)
                .map(player -> player.getName().getValue());
    }
}
