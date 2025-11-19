package com.bankcase.loan_service.application.port.out;

import com.bankcase.loan_service.domain.model.LoanInstallment;

import java.util.List;

public interface LoanInstallmentRepositoryPort {
    List<LoanInstallment> findByInstallmentsByLoanId(Long loanId);
}
