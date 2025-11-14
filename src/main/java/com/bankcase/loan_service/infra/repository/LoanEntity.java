package com.bankcase.loan_service.infra.repository;

import com.bankcase.loan_service.domain.model.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@NoArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private BigDecimal loanAmount;

    private BigDecimal remainingAmount;

    private BigDecimal interestRate;

    private int termInMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDateTime createdAt;

    public LoanEntity(Long id,
                      Long customerId,
                      BigDecimal loanAmount,
                      BigDecimal remainingAmount,
                      BigDecimal interestRate,
                      int termInMonths,
                      LoanStatus status,
                      LocalDateTime createdAt) {

        this.id = id;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.remainingAmount = remainingAmount;
        this.interestRate = interestRate;
        this.termInMonths = termInMonths;
        this.status = status;
        this.createdAt = createdAt;
    }
}
