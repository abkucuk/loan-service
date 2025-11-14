package com.bankcase.loan_service.application.port;


import com.bankcase.loan_service.domain.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepositoryPort {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    List<Loan> findByCustomerId(Long customerId);
}
