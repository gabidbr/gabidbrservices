package com.gabidbr.notification.rabbitmq;

import com.gabidbr.clients.notification.NotificationRequest;
import com.gabidbr.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;
    @RabbitListener(queues = "notification.queue")
    public void consumer(NotificationRequest notificationRequest){
        log.info("Consumer {} from queue", notificationRequest);
        notificationService.send(notificationRequest);
    }
}
