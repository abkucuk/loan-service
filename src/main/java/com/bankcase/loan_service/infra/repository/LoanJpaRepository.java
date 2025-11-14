package com.bankcase.loan_service.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByCustomerId(Long customerId);
}
