package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money Value Object Tests")
class MoneyTest extends UnitTest {

    @Test
    @DisplayName("Should create money with valid amount")
    void shouldCreateMoneyWithValidAmount() {
        Money money = new Money(new BigDecimal("100.50"));
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("Should throw exception when amount is null")
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null));
    }

    @Test
    @DisplayName("Should throw exception when amount is negative")
    void shouldThrowExceptionWhenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-100")));
    }

    @Test
    @DisplayName("Should throw exception when amount is zero")
    void shouldThrowExceptionWhenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should add money correctly")
    void shouldAddMoneyCorrectly() {
        Money money1 = new Money(new BigDecimal("100"));
        Money money2 = new Money(new BigDecimal("50"));

        Money result = money1.add(money2);

        assertEquals(new BigDecimal("150"), result.getAmount());
        // Original unchanged
        assertEquals(new BigDecimal("100"), money1.getAmount());
    }

    @Test
    @DisplayName("Should subtract money correctly")
    void shouldSubtractMoneyCorrectly() {
        Money money1 = new Money(new BigDecimal("100"));
        Money money2 = new Money(new BigDecimal("30"));

        Money result = money1.subtract(money2);

        assertEquals(new BigDecimal("70"), result.getAmount());
    }

    @Test
    @DisplayName("Should be equal when amounts are the same")
    void shouldBeEqualWhenAmountsAreSame() {
        Money money1 = new Money(new BigDecimal("100.00"));
        Money money2 = new Money(new BigDecimal("100.00"));

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when amounts are different")
    void shouldNotBeEqualWhenAmountsAreDifferent() {
        Money money1 = new Money(new BigDecimal("100"));
        Money money2 = new Money(new BigDecimal("50"));

        assertNotEquals(money1, money2);
    }
}
