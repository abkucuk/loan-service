package com.bankcase.loan_service.application.port.in;

import com.bankcase.loan_service.domain.model.Money;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * Tek bir taksit için ödeme sonucunu temsil eder.
 */
@Value
@Builder
public class PaidInstallmentDetail {

    /**
     * Ödenen taksidin ID'si (LoanInstallment.id)
     */
    Long installmentId;

    /**
     * Taksidin vade tarihi.
     */
    LocalDate dueDate;

    /**
     * İndirim ya da ceza uygulanmış gerçek ödeme tutarı.
     */
    Money paidAmount;
}
