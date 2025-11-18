package com.bankcase.loan_service.infra.adapter;

import com.bankcase.loan_service.application.port.LoanRepositoryPort;
import com.bankcase.loan_service.domain.model.InterestRate;
import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.domain.model.Money;
import com.bankcase.loan_service.domain.model.NumberOfInstallment;
import com.bankcase.loan_service.infra.mapper.LoanEntityMapper;
import com.bankcase.loan_service.infra.repository.LoanEntity;
import com.bankcase.loan_service.infra.repository.LoanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaLoanRepositoryAdapter implements LoanRepositoryPort {

    private final LoanJpaRepository repository;
    private final LoanEntityMapper mapper;

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = mapper.toEntity(loan);
        LoanEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Loan> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer has no loan"))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Loan toDomain(LoanEntity entity) {
        return Loan.create(
                entity.getId(),
                entity.getCustomerId(),
                Money.of(entity.getLoanAmount()),
                InterestRate.of(entity.getInterestRate()),
                NumberOfInstallment.of(entity.getNumberOfInstallment())
        );
    }
}
