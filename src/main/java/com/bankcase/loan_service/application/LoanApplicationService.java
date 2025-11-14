package com.bankcase.loan_service.application;

import com.bankcase.loan_service.application.dto.CreateLoanRequest;
import com.bankcase.loan_service.application.port.CustomerCreditCheckPort;
import com.bankcase.loan_service.application.port.LoanRepositoryPort;
import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.domain.model.enums.LoanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanRepositoryPort loanRepositoryPort;
    private final CustomerCreditCheckPort customerCreditCheckPort;

    public Loan createLoan(CreateLoanRequest request) {

        Long customerId = request.customerId();
        BigDecimal amount = request.amount();
        BigDecimal interestRate = request.interestRate();
        int term = 12; // default term in months; add to CreateLoanRequest if needed

        if (!customerCreditCheckPort.isCustomerEligible(customerId)) {
            throw new IllegalStateException("Customer is not eligible for loan.");
        }

        Loan loan = new Loan(
                null,
                customerId,
                amount,
                interestRate,
                term,
                LoanStatus.PENDING,
                LocalDateTime.now()
        );

        return loanRepositoryPort.save(loan);
    }

    public Loan approveLoan(Long id) {
        Loan loan = loanRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.approve();
        return loanRepositoryPort.save(loan);
    }

    public Loan rejectLoan(Long id) {
        Loan loan = loanRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.reject();
        return loanRepositoryPort.save(loan);
    }

    public Loan applyPayment(Long id, BigDecimal payment) {
        Loan loan = loanRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.applyPayment(payment);
        return loanRepositoryPort.save(loan);
    }
}
