package com.bankcase.loan_service.infra.controller;

import com.bankcase.loan_service.application.LoanApplicationService;
import com.bankcase.loan_service.application.port.in.CreateLoanRequest;
import com.bankcase.loan_service.application.port.in.PayLoanInstallmentsCommand;
import com.bankcase.loan_service.application.port.in.PayLoanInstallmentsResult;
import com.bankcase.loan_service.domain.model.Loan;
import com.bankcase.loan_service.domain.model.LoanInstallment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanApplicationService service;

    @PostMapping
    public Loan create(@RequestBody CreateLoanRequest request) {
        return service.createLoan(request);
    }

    @PostMapping("/{id}/approve")
    public Loan approve(@PathVariable Long id) {
        return service.approveLoan(id);
    }

    @PostMapping("/{id}/reject")
    public Loan reject(@PathVariable Long id) {
        return service.rejectLoan(id);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<Loan>> findByCustomerId(@PathVariable Long customerId) {
        return service.findByCustomerId(customerId);
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallment>> findByInstallments(@PathVariable Long loanId) {
        return service.findByInstallmentsByLoanId(loanId);
    }

    @PostMapping("/{loanId}/pay")
    public ResponseEntity<PayLoanInstallmentsResult> payInstallment(@PathVariable Long loanId, @RequestBody PayLoanInstallmentsCommand command) {
        return service.payInstallment(loanId, command);
    }
}
