package com.bankcase.loan_service.infra.mapper;

import com.bankcase.loan_service.domain.model.*;
import com.bankcase.loan_service.infra.repository.LoanEntity;
import com.bankcase.loan_service.infra.repository.LoanInstallmentEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanEntityMapper {

    public LoanEntity toEntity (Loan loan){
        LoanEntity entity =  new LoanEntity(
                loan.getCustomerId(),
                loan.getAmount().getValue(),
                loan.getInterestRate().getValue(),
                loan.getNumberOfInstallment().getValue(),
                loan.getStatus(),
                loan.getCreatedAt(),
                loan.isPaid()
        );

        loan.getInstallments().forEach(inst -> {
            LoanInstallmentEntity installmentEntity = inst.toCreateEntity(entity);
            entity.addInstallment(installmentEntity);
        });
        return entity;
    }

    public Loan toDomain(LoanEntity entity) {
        List<LoanInstallment> installments = new ArrayList<>();

        if (entity.getInstallmentList() != null) {
            entity.getInstallmentList().forEach(inst -> {
                installments.add(
                        LoanInstallment.restore(
                                inst.getId(),
                                inst.getLoanEntity().getId(),
                                Money.of(inst.getAmount()),
                                inst.getDueDate(),
                                inst.getPaymentDate(),
                                inst.isPaid()
                        )
                );
            });
        }

        return Loan.restore(
                entity.getId(),
                entity.getCustomerId(),
                Money.of(entity.getLoanAmount()),
                InterestRate.of(entity.getInterestRate()),
                NumberOfInstallment.of(entity.getNumberOfInstallment()),
                entity.getStatus(),
                entity.getCreatedAt(),
                installments,
                entity.isPaid()
        );
    }
}
