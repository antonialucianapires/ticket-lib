package com.alps.core.price;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    void shouldReturnOriginalPriceWhenNoDiscountsApplied() {
        BigDecimal originalAmount = new BigDecimal("100");
        Price price = new Price(originalAmount);
        assertEquals(originalAmount, price.calculateFinalPrice());
    }

    @Test
    void shouldApplySingleDiscountCorrectly() {
        BigDecimal originalAmount = new BigDecimal("100");
        BigDecimal discountAmount = new BigDecimal("10");
        DiscountRule discountRule = price -> price.subtract(discountAmount);

        Price price = new Price(originalAmount).addDiscountRule(discountRule);
        BigDecimal expectedFinalPrice = originalAmount.subtract(discountAmount);
        assertEquals(expectedFinalPrice, price.calculateFinalPrice());
    }

    @Test
    void shouldApplyMultipleDiscountsCorrectly() {
        BigDecimal originalAmount = new BigDecimal("100");
        BigDecimal firstDiscount = new BigDecimal("10");
        BigDecimal secondDiscount = new BigDecimal("5");
        DiscountRule firstDiscountRule = price -> price.subtract(firstDiscount);
        DiscountRule secondDiscountRule = price -> price.subtract(secondDiscount);

        Price price = new Price(originalAmount)
                .addDiscountRule(firstDiscountRule)
                .addDiscountRule(secondDiscountRule);

        BigDecimal expectedFinalPrice = originalAmount.subtract(firstDiscount).subtract(secondDiscount);
        assertEquals(expectedFinalPrice, price.calculateFinalPrice());
    }

    @Test
    void shouldReturnZeroOrPositiveFinalPrice() {
        BigDecimal originalAmount = new BigDecimal("15");
        BigDecimal discountAmount = new BigDecimal("20");
        DiscountRule discountRule = price -> price.subtract(discountAmount);

        Price price = new Price(originalAmount).addDiscountRule(discountRule);
        BigDecimal finalPrice = price.calculateFinalPrice();
        assertTrue(finalPrice.compareTo(BigDecimal.ZERO) >= 0);
    }
    
}
