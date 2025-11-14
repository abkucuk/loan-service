package com.bankcase.loan_service.domain.model;

import com.bankcase.loan_service.domain.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Loan {

    private Long id;
    private Long customerId;
    private BigDecimal loanAmount;
    private BigDecimal remainingAmount;
    private BigDecimal interestRate;
    private int termInMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;

    public Loan(Long id,
                Long customerId,
                BigDecimal loanAmount,
                BigDecimal interestRate,
                int termInMonths,
                LoanStatus status,
                LocalDateTime createdAt) {

        this.id = id;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.remainingAmount = loanAmount;
        this.interestRate = interestRate;
        this.termInMonths = termInMonths;
        this.status = status != null ? status : LoanStatus.PENDING;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public void approve() {
        if (this.status != LoanStatus.PENDING) {
            throw new IllegalStateException("Only PENDING loans can be approved.");
        }
        this.status = LoanStatus.APPROVED;
    }

    public void reject() {
        if (this.status != LoanStatus.PENDING) {
            throw new IllegalStateException("Only PENDING loans can be rejected.");
        }
        this.status = LoanStatus.REJECTED;
    }

    public void applyPayment(BigDecimal amount) {
        if (this.status != LoanStatus.APPROVED) {
            throw new IllegalStateException("Payment can only be applied to APPROVED loans.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        this.remainingAmount = this.remainingAmount.subtract(amount);

        if (this.remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.remainingAmount = BigDecimal.ZERO;
        }
    }
}
