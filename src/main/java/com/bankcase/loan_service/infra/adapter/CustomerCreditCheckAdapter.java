package com.bankcase.loan_service.infra.adapter;

import com.bankcase.loan_service.application.port.out.CustomerCreditCheckPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerCreditCheckAdapter implements CustomerCreditCheckPort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Boolean hasEnoughLimit(Long customerId, BigDecimal amount) {

        String url = "http://localhost:8082/customers/" + customerId + "/has-enough-limit?amount=" + amount;

        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            if (response != null && response.getBody() != null){
                return BooleanUtils.isTrue(response.getBody());
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
