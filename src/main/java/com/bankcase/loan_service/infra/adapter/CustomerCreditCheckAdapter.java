package com.bankcase.loan_service.infra.adapter;

import com.bankcase.loan_service.application.port.CustomerCreditCheckPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CustomerCreditCheckAdapter implements CustomerCreditCheckPort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean isCustomerEligible(Long customerId) {

        String url = "http://localhost:8082/customers/" + customerId + "/eligible";

        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }
}
