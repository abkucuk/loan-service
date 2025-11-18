package com.bankcase.loan_service.domain.model;

import com.bankcase.loan_service.infra.repository.LoanEntity;
import com.bankcase.loan_service.infra.repository.LoanInstallmentEntity;
import lombok.Getter;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
public class LoanInstallment {

    private final Long id;
    private final Long loanId;
    private final Money amount;
    private Money paidAmount;
    private final LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean paid;

    private LoanInstallment(
            Long id,
            Long loanId,
            Money amount,
            LocalDate dueDate,
            LocalDate paymentDate,
            boolean paid
    ) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paid = paid;
    }

    public static LoanInstallment create(Long id, Long loanId, Money amount, LocalDate dueDate) {
        return new LoanInstallment(
                id,
                loanId,
                amount,
                dueDate,
                null,
                false
        );
    }

    public boolean canBePaidOn(LocalDate today) {
        LocalDate maxPayableDate = today.plusMonths(3).withDayOfMonth(1);
        return !dueDate.isAfter(maxPayableDate);
    }

    public void pay(Money payment, LocalDate today) {
        if (paid) {
            throw new IllegalStateException("Installment already paid");
        }

        if (!canBePaidOn(today)) {
            throw new IllegalStateException("Installment cannot be paid; due date exceeds 3 months limit");
        }

        if (!payment.getValue().equals(amount.getValue())) {
            throw new IllegalArgumentException(
                    "Installment must be paid fully. Expected " + amount.getValue()
            );
        }

        this.paidAmount = payment;
        this.paymentDate = today;
        this.paid = true;
    }

    public boolean isDueBefore(LocalDate today) {
        return dueDate.isBefore(today);
    }

    public LoanInstallmentEntity toCreateEntity (LoanEntity loanEntity){
        return LoanInstallmentEntity.builder()
                .id(null)
                .loanEntity(loanEntity)
                .amount(this.getAmount().getValue())
                .dueDate(this.getDueDate())
                .paymentDate(this.getPaymentDate())
                .isPaid(this.isPaid())
                .build();
    }

    public static LoanInstallment toDomain(LoanInstallmentEntity entity){
        return new LoanInstallment(
                entity.getId(),
                entity.getLoanEntity().getId(),
                Money.of(entity.getAmount()),
                entity.getDueDate(),
                entity.getPaymentDate(),
                entity.isPaid()
        );
    }

    public static LoanInstallment restore(
            Long id,
            Long loanId,
            Money amount,
            LocalDate dueDate,
            LocalDate paymentDate,
            boolean paid
    ) {
        return new LoanInstallment(
                id,
                loanId,
                amount,
                dueDate,
                paymentDate,
                paid
        );
    }
}
