package cat.opteams.blackjack.domain.model.valueobject;

public record Card(Suit suit, Rank rank) {

    public int getValue() {
        return rank.getValue();
    }

    public boolean isAce() {
        return rank.isAce();
    }

    @Override
    public String toString() {
        return rank.name() + " of " + suit.name();
    }
}
