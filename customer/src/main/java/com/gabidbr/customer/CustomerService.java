package com.gabidbr.customer;

import com.gabidbr.clients.fraud.FraudCheckResponse;
import com.gabidbr.clients.fraud.FraudClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate, FraudClient fraudClient) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // TODO check if email is valid
        // TODO check if email not token
        // TODO check if fraudster
        customerRepository.saveAndFlush(customer);

//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );

        FraudCheckResponse fraudResponse = fraudClient.isFraudster(customer.getId());

        if(fraudResponse.isFraudster()){
            throw new IllegalStateException("Customer is a fraudster");
        }

        // TODO send notification
    }
}
