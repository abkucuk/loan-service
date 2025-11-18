package com.bankcase.loan_service.infra.mapper;

import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.infra.repository.LoanEntity;
import com.bankcase.loan_service.infra.repository.LoanInstallmentEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LoanEntityMapper {

    public LoanEntity toEntity (Loan loan){
        LoanEntity entity =  new LoanEntity(
                loan.getCustomerId(),
                loan.getAmount().getValue(),
                loan.getInterestRate().getValue(),
                loan.getNumberOfInstallment().getValue(),
                loan.getStatus(),
                loan.getCreatedAt()
        );

        loan.getInstallments().forEach(inst -> {
            LoanInstallmentEntity installmentEntity = inst.toCreateEntity(entity);
            entity.addInstallment(installmentEntity);
        });
        return entity;
    }
}
