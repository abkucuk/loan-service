package com.bankcase.loan_service.application;

import com.bankcase.loan_service.application.port.in.CreateLoanRequest;
import com.bankcase.loan_service.application.port.in.PayLoanInstallmentsCommand;
import com.bankcase.loan_service.application.port.in.PayLoanInstallmentsResult;
import com.bankcase.loan_service.application.port.out.CustomerCreditCheckPort;
import com.bankcase.loan_service.application.port.out.LoanInstallmentRepositoryPort;
import com.bankcase.loan_service.application.port.out.LoanRepositoryPort;
import com.bankcase.loan_service.domain.model.*;
import com.bankcase.loan_service.infra.mapper.PaidInstallmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanRepositoryPort loanRepositoryPort;
    private final CustomerCreditCheckPort customerCreditCheckPort;
    private final LoanInstallmentRepositoryPort loanInstallmentRepositoryPort;
    private final PaidInstallmentMapper paidInstallmentMapper;

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

    public ResponseEntity<List<LoanInstallment>> findByInstallmentsByLoanId(Long loanId) {
        List<LoanInstallment> installmentList = loanInstallmentRepositoryPort.findByInstallmentsByLoanId(loanId);
        return ResponseEntity.ok(installmentList);
    }

    public ResponseEntity<PayLoanInstallmentsResult> payInstallment(Long loanId, PayLoanInstallmentsCommand command) {
        Loan loan = loanRepositoryPort.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        List<LoanInstallment> paidInstallments = loan.payInstallment(command.getAmount());
        loanRepositoryPort.save(loan);
        return ResponseEntity.ok(PayLoanInstallmentsResult.builder()
                .paidInstallmentCount(paidInstallments.size())
                .totalPaidAmount(
                        Money.of(paidInstallments.stream()
                                .map(LoanInstallment::getPaidAmount)
                                .map(Money::getValue)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                )
                .loanFullyPaid(loan.isPaid())
                .paidInstallments(paidInstallmentMapper.toPayLoanInstallmentsResult(paidInstallments))
                .build());
    }
}
