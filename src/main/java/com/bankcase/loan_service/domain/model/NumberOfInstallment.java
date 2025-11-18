package com.bankcase.loan_service.domain.model;


import lombok.Value;

import java.util.List;

@Value
public class NumberOfInstallment {

    Integer value;

    private static final List<Integer> VALID_INSTALLMENTS = List.of(6, 9, 12, 24);

    private NumberOfInstallment(Integer value) {
        if (!VALID_INSTALLMENTS.contains(value)) {
            throw new IllegalArgumentException(
                    "Invalid installments count: " + value + ". Valid values: " + VALID_INSTALLMENTS
            );
        }
        this.value = value;
    }

    public static NumberOfInstallment of(Integer value) {
        return new NumberOfInstallment(value);
    }
}
