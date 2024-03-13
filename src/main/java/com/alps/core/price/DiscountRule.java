package com.alps.core.price;

import java.math.BigDecimal;

public interface DiscountRule {
    BigDecimal apply(BigDecimal amount);
}
