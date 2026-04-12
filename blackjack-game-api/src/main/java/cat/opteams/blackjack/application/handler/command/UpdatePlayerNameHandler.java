package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.application.command.UpdatePlayerNameCommand;
import cat.opteams.blackjack.application.mapper.RankingResponseMapper;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import cat.opteams.blackjack.application.validator.UpdatePlayerNameValidator;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePlayerNameHandler {

    private final UpdatePlayerNameValidator validator;
    private final PlayerRepositoryPort playerRepository;
    private final RankingResponseMapper responseMapper;

    public Mono<RankingEntryResponse> handle(UpdatePlayerNameCommand command) {
        log.debug("Handling UpdatePlayerNameCommand for player: {}", command.playerId());

        validator.validate(command);

        PlayerId playerId = new PlayerId(command.playerId());
        PlayerName newName = new PlayerName(command.newName());

        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Player not found: " + command.playerId())))
                .flatMap(player -> playerRepository.existsByName(newName)
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("Player name already exists: " + command.newName()));
                            }
                            return Mono.just(player);
                        }))
                .map(player -> {
                    player.updateName(newName);
                    return player;
                })
                .flatMap(playerRepository::save)
                .map(responseMapper::toResponse)
                .doOnSuccess(response -> log.info("Player name updated successfully: {}", response.playerId()))
                .doOnError(error -> log.error("Error updating player name: {}", error.getMessage()));
    }
}
