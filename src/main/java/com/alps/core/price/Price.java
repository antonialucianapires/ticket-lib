package com.alps.core.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the price of an item, which can be a reservation or a ticket
 * associated with a seat.
 * This class allows for the application of various discount rules to compute a
 * final price.
 */
public class Price {
    private final BigDecimal amount;
    private final List<DiscountRule> discountRules;

    /**
     * Constructs a Price object with an initial amount.
     *
     * @param amount The initial price amount without any discounts applied.
     */
    public Price(BigDecimal amount) {
        this.amount = amount;
        this.discountRules = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructs a Price object with an initial amount and a list of discount
     * rules.
     *
     * @param amount        The initial price amount without any discounts applied.
     * @param discountRules The list of discount rules to be applied to this price.
     */
    private Price(BigDecimal amount, List<DiscountRule> discountRules) {
        this.amount = amount;
        this.discountRules = discountRules;
    }

    /**
     * Adds a discount rule to this price.
     *
     * @param discountRule The discount rule to be added.
     * @return A new Price object with the added discount rule.
     */
    public Price addDiscountRule(DiscountRule discountRule) {
        List<DiscountRule> newDiscountRules = new ArrayList<>(this.discountRules);
        newDiscountRules.add(discountRule);
        return new Price(this.amount, newDiscountRules);
    }

    /**
     * Calculates the final price after applying all the discount rules.
     *
     * @return The final price after discounts have been applied. This value is
     *         guaranteed to be non-negative.
     */
    public BigDecimal calculateFinalPrice() {
        BigDecimal finalPrice = discountRules.stream()
                .reduce(amount, (currentPrice, rule) -> rule.apply(currentPrice), BigDecimal::add);
        return finalPrice.compareTo(BigDecimal.ZERO) >= 0 ? finalPrice : BigDecimal.ZERO;

    }
}
