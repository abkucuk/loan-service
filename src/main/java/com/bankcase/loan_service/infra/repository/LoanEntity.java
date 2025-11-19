package com.bankcase.loan_service.infra.repository;

import com.bankcase.loan_service.domain.model.LoanInstallment;
import com.bankcase.loan_service.domain.model.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private BigDecimal interestRate;

    private Integer numberOfInstallment;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDateTime createdAt;

    private boolean isPaid;

    @OneToMany(
            mappedBy = "loanEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<LoanInstallmentEntity> installmentList;

    public LoanEntity(Long customerId,
                      BigDecimal loanAmount,
                      BigDecimal interestRate,
                      Integer numberOfInstallment,
                      LoanStatus status,
                      LocalDateTime createdAt,
                      boolean isPaid) {

        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.numberOfInstallment = numberOfInstallment;
        this.status = status;
        this.createdAt = createdAt;
        this.isPaid = isPaid;
    }

    public void addInstallment(LoanInstallmentEntity installment){
        if (this.installmentList == null) {
            this.installmentList = new ArrayList<>();
        }
        this.installmentList.add(installment);
        installment.setLoanEntity(this);
    }

    public void removeInstallment(LoanInstallmentEntity installment){
        this.installmentList.remove(installment);
        installment.setLoanEntity(null);
    }
}
