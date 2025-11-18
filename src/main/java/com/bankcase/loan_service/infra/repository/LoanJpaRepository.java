package com.bankcase.loan_service.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, Long> {

    Optional<List<LoanEntity>> findByCustomerId(Long customerId);
}
