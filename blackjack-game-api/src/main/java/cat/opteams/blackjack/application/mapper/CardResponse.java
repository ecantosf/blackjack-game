package cat.opteams.blackjack.application.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardResponse(
        String suit,
        String rank,
        String symbol,
        Integer value
) {}
