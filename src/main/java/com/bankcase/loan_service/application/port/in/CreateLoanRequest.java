package com.bankcase.loan_service.application.port.in;

import java.math.BigDecimal;

public record CreateLoanRequest(
        Long customerId,
        BigDecimal amount,
        BigDecimal interestRate,
        Integer installments
) {}
