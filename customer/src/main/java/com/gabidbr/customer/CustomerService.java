package com.gabidbr.customer;

import com.gabidbr.amqp.RabbitMqMessageProducer;
import com.gabidbr.clients.fraud.FraudCheckResponse;
import com.gabidbr.clients.fraud.FraudClient;
import com.gabidbr.clients.notification.NotificationClient;
import com.gabidbr.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              RestTemplate restTemplate,
                              FraudClient fraudClient,
                              RabbitMqMessageProducer rabbitMqMessageProducer) {
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

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getFirstName(),
                String.format("Welcome %s to our awesome platform",
                        customer.getFirstName())
        );
//        notificationClient.sendNotification(notificationRequest);
        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
