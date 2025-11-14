package com.bankcase.loan_service.infra.adapter;

import com.bankcase.loan_service.application.port.LoanRepositoryPort;
import com.bankcase.loan_service.domain.model.Loan;
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

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = new LoanEntity(
                loan.getId(),
                loan.getCustomerId(),
                loan.getLoanAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getTermInMonths(),
                loan.getStatus(),
                loan.getCreatedAt()
        );

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
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Loan toDomain(LoanEntity entity) {
        return new Loan(
                entity.getId(),
                entity.getCustomerId(),
                entity.getLoanAmount(),
                entity.getInterestRate(),
                entity.getTermInMonths(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
