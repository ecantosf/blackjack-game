package cat.opteams.blackjack.application.handler.query;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.query.GetGameQuery;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import cat.opteams.blackjack.shared.exception.GameNotFoundException;
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
@DisplayName("GetGameQueryHandler Tests")
class GetGameQueryHandlerTest extends UnitTest {

    @Mock
    private GameRepositoryPort gameRepository;

    @Mock
    private PlayerRepositoryPort playerRepository;

    @Mock
    private GameResponseMapper responseMapper;

    @InjectMocks
    private GetGameQueryHandler handler;

    private Game game;
    private Player player;
    private String gameId;
    private GameResponse gameResponse;

    @BeforeEach
    void setUp() {
        game = GameTestBuilder.aGame().build();
        player = TestDataFactory.createDefaultPlayer();
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
    }

    @Test
    @DisplayName("Should retrieve game successfully when exists")
    void shouldRetrieveGameSuccessfullyWhenExists() {
        GetGameQuery query = new GetGameQuery(gameId);
        when(gameRepository.findById(any())).thenReturn(Mono.just(game));
        when(playerRepository.findById(any())).thenReturn(Mono.just(player));
        when(responseMapper.toResponse(any(Game.class), any(String.class))).thenReturn(gameResponse);

        StepVerifier.create(handler.handle(query))
                .expectNext(gameResponse)
                .verifyComplete();

        verify(gameRepository, times(1)).findById(any());
        verify(playerRepository, times(1)).findById(any());
        verify(responseMapper, times(1)).toResponse(any(Game.class), any(String.class));
    }

    @Test
    @DisplayName("Should throw exception when game not found")
    void shouldThrowExceptionWhenGameNotFound() {
        GetGameQuery query = new GetGameQuery(gameId);
        when(gameRepository.findById(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(query))
                .expectError(GameNotFoundException.class)
                .verify();

        verify(gameRepository, times(1)).findById(any());
        verify(playerRepository, never()).findById(any());
        verify(responseMapper, never()).toResponse(any(), any());
    }
}
