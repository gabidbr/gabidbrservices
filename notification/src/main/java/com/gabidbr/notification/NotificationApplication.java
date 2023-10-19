package com.gabidbr.notification;

import com.gabidbr.amqp.RabbitMqMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(
        scanBasePackages = {
                "com.gabidbr.notification",
                "com.gabidbr.amqp"
        }
)
@EnableEurekaClient
public class NotificationApplication {

    public static void main(String[] args){
        SpringApplication.run(NotificationApplication.class, args);
    }

//    CommandLineRunner commandLineRunner(
//            RabbitMqMessageProducer producer,
//            NotificationConfig notificationConfig){
//        return args -> {
//            producer.publish(
//                    new Person("Gabriel", 25),
//                    notificationConfig.getInternalExchange(),
//                    notificationConfig.getInternalNotificationRoutingKey()
//            );
//        };
//    }
//
//    record Person(String name, int age){}
}
