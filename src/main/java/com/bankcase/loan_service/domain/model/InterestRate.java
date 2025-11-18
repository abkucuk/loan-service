package com.bankcase.loan_service.domain.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class InterestRate {

    BigDecimal value;

    private static final BigDecimal MIN = new BigDecimal("0.1");
    private static final BigDecimal MAX = new BigDecimal("0.5");

    private InterestRate(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public static InterestRate of(BigDecimal value) {
        return new InterestRate(value);
    }

    private static void validate(BigDecimal rate) {
        if (rate == null) {
            throw new IllegalArgumentException("Interest rate cannot be null");
        }

        if (rate.compareTo(MIN) < 0 || rate.compareTo(MAX) > 0) {
            throw new IllegalArgumentException(
                    "Interest rate must be between 0.1 and 0.5"
            );
        }
    }

    public BigDecimal asDecimal() {
        return value;
    }
}
