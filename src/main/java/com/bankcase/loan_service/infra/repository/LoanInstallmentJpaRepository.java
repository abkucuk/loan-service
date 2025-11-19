package com.bankcase.loan_service.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanInstallmentJpaRepository extends JpaRepository<LoanInstallmentEntity, Long> {
    @Query("SELECT i FROM LoanInstallmentEntity i WHERE i.loanEntity.id = ?1 ORDER BY i.dueDate ASC")
    Optional<List<LoanInstallmentEntity>> findByLoanIdOrderByDueDateAsc(Long loanId);
}
