package cat.opteams.blackjack.domain.service;

import cat.opteams.blackjack.domain.model.valueobject.Card;

public record DrawResult(Card card, Deck remainingDeck) {}