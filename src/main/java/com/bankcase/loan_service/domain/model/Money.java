package com.bankcase.loan_service.domain.model;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Value
public class Money {

    BigDecimal value;

    private Money(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Money value cannot be null.");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Money value must be positive.");
        }

        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }


    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public boolean isGreaterThan(Money other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(Money other) {
        return this.value.compareTo(other.value) < 0;
    }
}
