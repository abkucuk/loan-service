package com.bankcase.loan_service.application.port;

import java.math.BigDecimal;

public interface CustomerCreditCheckPort {

    Boolean hasEnoughLimit(Long customerId, BigDecimal amount);
}
