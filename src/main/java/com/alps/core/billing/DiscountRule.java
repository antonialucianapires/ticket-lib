package com.alps.core.billing;

import java.math.BigDecimal;

import com.alps.core.ticket.Ticket;

public interface DiscountRule {
    BigDecimal calculateDiscount(Ticket ticket);
}
