package cat.opteams.blackjack.application.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameResponse(
        String id,
        String playerId,
        String playerName,
        List<CardResponse> playerCards,
        Integer playerValue,
        List<CardResponse> dealerCards,
        Integer dealerValue,
        BigDecimal bet,
        String status,
        String winner,
        LocalDateTime createdAt,
        LocalDateTime finishedAt
) {}
