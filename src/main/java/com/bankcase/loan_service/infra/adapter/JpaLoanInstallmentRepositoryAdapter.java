package com.bankcase.loan_service.infra.adapter;

import com.bankcase.loan_service.application.port.out.LoanInstallmentRepositoryPort;
import com.bankcase.loan_service.domain.model.LoanInstallment;
import com.bankcase.loan_service.infra.repository.LoanInstallmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaLoanInstallmentRepositoryAdapter implements LoanInstallmentRepositoryPort {
    private final LoanInstallmentJpaRepository loanInstallmentJpaRepository;

    @Override
    public List<LoanInstallment> findByInstallmentsByLoanId(Long loanId) {
        return loanInstallmentJpaRepository.findByLoanIdOrderByDueDateAsc(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan has no installments"))
                .stream().map(LoanInstallment::toDomain).toList();
    }
}
