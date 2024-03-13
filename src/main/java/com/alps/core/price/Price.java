package com.alps.core.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Price {
    private final BigDecimal amount;
    private final List<DiscountRule> discountRules;

    public Price(BigDecimal amount) {
        this.amount = amount;
        this.discountRules = new CopyOnWriteArrayList<>();
    }

    public Price addDiscountRule(DiscountRule discountRule) {
        List<DiscountRule> newDiscountRules = new ArrayList<>(this.discountRules);
        newDiscountRules.add(discountRule);
        return new Price(this.amount, newDiscountRules);
    }

    private Price(BigDecimal amount, List<DiscountRule> discountRules) {
        this.amount = amount;
        this.discountRules = discountRules;
    }

    public BigDecimal calculateFinalPrice() {
        BigDecimal finalPrice = discountRules.stream()
                .reduce(amount, (currentPrice, rule) -> rule.apply(currentPrice), BigDecimal::add);
        return finalPrice.compareTo(BigDecimal.ZERO) >= 0 ? finalPrice : BigDecimal.ZERO;

    }
}
