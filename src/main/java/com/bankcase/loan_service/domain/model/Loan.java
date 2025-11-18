package com.bankcase.loan_service.domain.model;

import com.bankcase.loan_service.domain.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bankcase.loan_service.infra.repository.LoanInstallmentEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Loan {

    private final Long id;
    private final Long customerId;
    private Money amount;
    private InterestRate interestRate;
    private NumberOfInstallment numberOfInstallment;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private List<LoanInstallment> installments;


    private static final List<Integer> VALID_INSTALLMENTS = List.of(6,9,12,24);

    private Loan(
            Long id,
            Long customerId,
            Money amount,
            InterestRate interestRate,
            NumberOfInstallment numberOfInstallment,
            LoanStatus status,
            LocalDateTime createdAt,
            List<LoanInstallment> installments
    ) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.numberOfInstallment = numberOfInstallment;
        this.status = status;
        this.createdAt = createdAt;
        this.installments = installments;
    }

    public static Loan create(
            Long id,
            Long customerId,
            Money amount,
            InterestRate interestRate,
            NumberOfInstallment numberOfInstallment
    ) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (interestRate == null) {
            throw new IllegalArgumentException("Interest rate cannot be null");
        }

        if (numberOfInstallment == null) {
            throw new IllegalArgumentException("Installments cannot be null");
        }

        BigDecimal totalAmount = amount.getValue()
                .multiply(BigDecimal.ONE.add(interestRate.getValue()));

        BigDecimal installmentAmount = totalAmount
                .divide(BigDecimal.valueOf(numberOfInstallment.getValue()), 2, RoundingMode.HALF_UP);

        List<LoanInstallment> installments = createInstallments(numberOfInstallment, installmentAmount);


        if (installmentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Installment amount must be positive.");
        }

        return new Loan(
                id,
                customerId,
                amount,
                interestRate,
                numberOfInstallment,
                LoanStatus.APPROVED,
                LocalDateTime.now(),
                installments
        );
    }

    private static List<LoanInstallment> createInstallments(NumberOfInstallment numberOfInstallment, BigDecimal installmentAmount) {
        List<LoanInstallment> installments = new ArrayList<>();
        LocalDate nextDueDate = generateNextDueDate(LocalDate.now());
        for (int i = 0; i < numberOfInstallment.getValue(); i++) {
            installments.add(
                    LoanInstallment.create(
                            null,
                            null,
                            Money.of(installmentAmount),
                            nextDueDate
                    )
            );
            nextDueDate = generateNextDueDate(nextDueDate);
        }
        return installments;
    }

    public static LocalDate generateNextDueDate(LocalDate baseDate) {
        if (baseDate == null) {
            throw new IllegalArgumentException("baseDate cannot be null");
        }
        LocalDate nextMonth = baseDate.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }

    public static Loan restore(Long id, Long customerId, Money amount, InterestRate interestRate, NumberOfInstallment numberOfInstallment, LoanStatus status, LocalDateTime createdAt, List<LoanInstallment> installments) {
        return new Loan(
                id,
                customerId,
                amount,
                interestRate,
                numberOfInstallment,
                status,
                createdAt,
                installments
        );
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
}
