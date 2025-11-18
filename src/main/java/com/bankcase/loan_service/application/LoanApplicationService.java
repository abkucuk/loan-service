package com.bankcase.loan_service.application;

import com.bankcase.loan_service.application.dto.CreateLoanRequest;
import com.bankcase.loan_service.application.port.CustomerCreditCheckPort;
import com.bankcase.loan_service.application.port.LoanRepositoryPort;
import com.bankcase.loan_service.domain.model.NumberOfInstallment;
import com.bankcase.loan_service.domain.model.InterestRate;
import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanRepositoryPort loanRepositoryPort;
    private final CustomerCreditCheckPort customerCreditCheckPort;

    public Loan createLoan(CreateLoanRequest request) {

        Long customerId = request.customerId();
        Money amount = Money.of(request.amount());
        InterestRate interestRate = InterestRate.of(request.interestRate());
        NumberOfInstallment numberOfInstallment = NumberOfInstallment.of(request.installments());

        Boolean hasEnoughLimit = customerCreditCheckPort.hasEnoughLimit(customerId, amount.getValue());

        if (!hasEnoughLimit) {
            throw new IllegalStateException("Insufficient credit available for customer ID: " + customerId);
        }

        Loan loan = Loan.create(
                null,
                customerId,
                amount,
                interestRate,
                numberOfInstallment
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

        // TODO: Implement payment application logic
        //loan.applyPayment(payment);
        return loanRepositoryPort.save(loan);
    }

    public ResponseEntity<List<Loan>> findByCustomerId(Long customerId) {
        List<Loan> loans = loanRepositoryPort.findByCustomerId(customerId);
        return ResponseEntity.ok(loans);
    }
}
