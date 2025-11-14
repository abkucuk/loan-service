package com.bankcase.loan_service.application.dto;

import java.math.BigDecimal;

public record CreateLoanRequest(
        Long customerId,
        BigDecimal amount,
        BigDecimal interestRate
) {}
