package com.bankcase.loan_service.application.dto;

import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.domain.model.enums.LoanStatus;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public record LoanResponse(
        Long id,
        Long customerId,
        BigDecimal amount,
        BigDecimal remainingAmount,
        BigDecimal interestRate,
        LoanStatus status,
        LocalDateTime createdAt
) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getCustomerId(),
                loan.getLoanAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getStatus(),
                loan.getCreatedAt()
        );
    }
}
