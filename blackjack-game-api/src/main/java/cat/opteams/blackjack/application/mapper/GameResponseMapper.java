package cat.opteams.blackjack.application.mapper;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.Card;
import cat.opteams.blackjack.domain.model.valueobject.Hand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameResponseMapper {

    public GameResponse toResponse(Game game, String playerName) {
        if (game == null) {
            return null;
        }

        return new GameResponse(
                game.getId().getValue().toString(),
                game.getPlayerId().getValue().toString(),
                playerName,
                toCardResponseList(game.getPlayerHand()),
                game.getPlayerHand().calculateValue(),
                toCardResponseListForDealer(game.getDealerHand(), game.isFinished()),
                game.getDealerHand().calculateValue(),
                game.getBet().getAmount(),
                game.getStatus().name(),
                game.getWinner() != null ? game.getWinner().name() : null,
                game.getCreatedAt(),
                game.getFinishedAt()
        );
    }

    private List<CardResponse> toCardResponseList(Hand hand) {
        return hand.getCards().stream()
                .map(this::toCardResponse)
                .collect(Collectors.toList());
    }

    private List<CardResponse> toCardResponseListForDealer(Hand hand, boolean isFinished) {
        if (!isFinished && hand.getCards().size() > 1) {
            return List.of(toCardResponse(hand.getCards().get(0)));
        }
        return toCardResponseList(hand);
    }

    private CardResponse toCardResponse(Card card) {
        return new CardResponse(
                card.suit().name(),
                card.rank().name(),
                card.suit().getSymbol(),
                card.getValue()
        );
    }
}
