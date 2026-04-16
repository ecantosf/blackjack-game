package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.application.command.PlayCommand;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.validator.PlayCommandValidator;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.port.outgoing.DeckProviderPort;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import cat.opteams.blackjack.domain.service.Deck;
import cat.opteams.blackjack.testutil.GameTestBuilder;
import cat.opteams.blackjack.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayHandler Tests")
class PlayHandlerTest extends UnitTest {

    @Mock
    private PlayCommandValidator validator;

    @Mock
    private GameRepositoryPort gameRepository;

    @Mock
    private PlayerRepositoryPort playerRepository;

    @Mock
    private DeckProviderPort deckProvider;

    @Mock
    private GameResponseMapper responseMapper;

    @InjectMocks
    private PlayHandler handler;

    private Player player;
    private Game game;
    private String gameId;
    private GameResponse gameResponse;

    @BeforeEach
    void setUp() {
        player = TestDataFactory.createDefaultPlayer();
        game = GameTestBuilder.aGame().build();
        gameId = game.getId().getValue().toString();
        gameResponse = new GameResponse(
                gameId,
                player.getId().getValue().toString(),
                player.getName().getValue(),
                null, null, null, null,
                game.getBet().getAmount(),
                game.getStatus().name(),
                null,
                game.getCreatedAt(),
                null
        );
        doNothing().when(validator).validate(any());
    }

    @Test
    @DisplayName("Should execute HIT action successfully")
    void shouldExecuteHitActionSuccessfully() {
        Deck deck = new Deck(42L);
        when(gameRepository.findById(any(GameId.class))).thenReturn(Mono.just(game));
        when(playerRepository.findById(any())).thenReturn(Mono.just(player));
        when(deckProvider.getNewDeck()).thenReturn(Mono.just(deck));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(responseMapper.toResponse(any(Game.class), anyString())).thenReturn(gameResponse);

        PlayCommand command = new PlayCommand(gameId, PlayCommand.Action.HIT);

        StepVerifier.create(handler.handle(command))
                .expectNextCount(1)
                .verifyComplete();

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("Should execute STAND action successfully")
    void shouldExecuteStandActionSuccessfully() {
        Deck deck = new Deck(42L);
        when(gameRepository.findById(any(GameId.class))).thenReturn(Mono.just(game));
        when(playerRepository.findById(any())).thenReturn(Mono.just(player));
        when(deckProvider.getNewDeck()).thenReturn(Mono.just(deck));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));
        when(responseMapper.toResponse(any(Game.class), anyString())).thenReturn(gameResponse);

        PlayCommand command = new PlayCommand(gameId, PlayCommand.Action.STAND);

        StepVerifier.create(handler.handle(command))
                .expectNextCount(1)
                .verifyComplete();

        verify(gameRepository, times(1)).save(any(Game.class));
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when game not found")
    void shouldThrowExceptionWhenGameNotFound() {
        when(gameRepository.findById(any(GameId.class))).thenReturn(Mono.empty());

        PlayCommand command = new PlayCommand(gameId, PlayCommand.Action.HIT);

        StepVerifier.create(handler.handle(command))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(gameRepository, never()).save(any(Game.class));
        verify(playerRepository, never()).save(any(Player.class));
    }
}
