package com.bankcase.loan_service.application.port.in;

import com.bankcase.loan_service.domain.model.Money;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PayLoanInstallmentsResult {

    /**
     * Bu işlemde kaç adet taksit ödendi?
     */
    int paidInstallmentCount;

    /**
     * İndirim / ceza dahil olmak üzere müşterinin gerçekte ödediği toplam tutar.
     */
    Money totalPaidAmount;

    /**
     * Bu ödeme sonrası kredi tamamen kapanmış mı?
     */
    boolean loanFullyPaid;

    /**
     * Ödenen taksitlerin detayları.
     */
    List<PaidInstallmentDetail> paidInstallments;
}
