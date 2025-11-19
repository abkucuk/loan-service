package com.bankcase.loan_service.application.port.in;


import com.bankcase.loan_service.domain.model.Money;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class PayLoanInstallmentsCommand {

    /**
     * Ödeme yapılacak kredinin ID'si (Loan aggregate id)
     */
    Long loanId;

    /**
     * Müşterinin bu işlemde ödemek istediği toplam tutar.
     * Domain bu tutarı taksitlere dağıtacak.
     */
    Money amount;
}
