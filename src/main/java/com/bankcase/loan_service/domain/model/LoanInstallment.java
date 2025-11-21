package com.bankcase.loan_service.domain.model;

import com.bankcase.loan_service.infra.repository.LoanEntity;
import com.bankcase.loan_service.infra.repository.LoanInstallmentEntity;
import lombok.Getter;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class LoanInstallment {

    private static final Logger logger = LoggerFactory.getLogger(LoanInstallment.class);

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
            Money paidAmount,
            LocalDate dueDate,
            LocalDate paymentDate,
            boolean paid
    ) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.paidAmount = paidAmount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paid = paid;
    }

    public static LoanInstallment create(Long id, Long loanId, Money amount, Money paidAmount, LocalDate dueDate) {
        return new LoanInstallment(
                id,
                loanId,
                amount,
                paidAmount,
                dueDate,
                null,
                false
        );
    }

    public boolean canBePaidOn(LocalDate today) {
        LocalDate maxPayableDate = today.plusMonths(3).withDayOfMonth(1);
        return !dueDate.isAfter(maxPayableDate);
    }

    public Boolean pay(Money payment, LocalDate today) {
        if (paid) {
            logger.info("Installment already paid");
            return false;
        }

        if (!canBePaidOn(today)) {
            logger.info("Installment cannot be paid; due date exceeds 3 months limit");
            return false;
        }

        if (payment.getValue().compareTo(amount.getValue()) < 0) {
            logger.info("Installment must be paid fully. Expected " + amount.getValue() );
            return false;
        }

        this.paidAmount = calculatePaidAmount();
        this.paymentDate = today;
        this.paid = true;

        return true;
    }

    private Money calculatePaidAmount() {
        Money payment = Money.of(this.getAmount().getValue());
       if (dueDate.isBefore(LocalDate.now())){
           BigDecimal penalty = payment.getValue()
                   .multiply(BigDecimal.valueOf(0.001))
                   .multiply(BigDecimal.valueOf(Math.abs(LocalDate.now().toEpochDay() - this.getDueDate().toEpochDay())));
           return payment.add(Money.of(penalty));

       }
       if (dueDate.isAfter(LocalDate.now())){
           BigDecimal discount = payment.getValue()
                   .multiply(BigDecimal.valueOf(0.001))
                   .multiply(BigDecimal.valueOf(Math.abs(this.getDueDate().toEpochDay()- LocalDate.now().toEpochDay())));
           return payment.subtract(Money.of(discount));
       }
        return payment;
    }

    public LoanInstallmentEntity toCreateEntity (LoanEntity loanEntity){
        return LoanInstallmentEntity.builder()
                .id(null)
                .loanEntity(loanEntity)
                .amount(this.getAmount().getValue())
                .paidAmount(this.getPaidAmount() != null ? this.getPaidAmount().getValue() : BigDecimal.ZERO)
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
                Money.of(entity.getPaidAmount()),
                entity.getDueDate(),
                entity.getPaymentDate(),
                entity.isPaid()
        );
    }

    public static LoanInstallment restore(
            Long id,
            Long loanId,
            Money amount,
            Money paidAmount,
            LocalDate dueDate,
            LocalDate paymentDate,
            boolean paid
    ) {
        return new LoanInstallment(
                id,
                loanId,
                amount,
                paidAmount,
                dueDate,
                paymentDate,
                paid
        );
    }
}
