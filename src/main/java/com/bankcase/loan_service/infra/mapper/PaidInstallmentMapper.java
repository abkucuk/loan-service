package com.bankcase.loan_service.infra.mapper;

import com.bankcase.loan_service.application.port.in.PaidInstallmentDetail;
import com.bankcase.loan_service.application.port.in.PayLoanInstallmentsResult;
import com.bankcase.loan_service.domain.model.LoanInstallment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaidInstallmentMapper {
    public List<PaidInstallmentDetail> toPayLoanInstallmentsResult(List<LoanInstallment> installments) {
        return installments.stream()
                .map(inst -> PaidInstallmentDetail.builder()
                        .installmentId(inst.getId())
                        .dueDate(inst.getDueDate())
                        .paidAmount(inst.getPaidAmount())
                        .build())
                .toList();
    }
}
