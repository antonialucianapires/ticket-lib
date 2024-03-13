package com.alps.core.price;

import java.math.BigDecimal;

/**
 * Represents a rule that can be applied to a Price to calculate a discount.
 */
public interface DiscountRule {
    /**
     * Applies the discount rule to a given amount.
     *
     * @param amount The original price amount before the discount is applied.
     * @return The amount after the discount has been applied.
     */
    BigDecimal apply(BigDecimal amount);
}
