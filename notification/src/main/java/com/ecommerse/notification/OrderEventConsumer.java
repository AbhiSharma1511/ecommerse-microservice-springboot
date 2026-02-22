package com.ecommerse.notification;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(Map<String, Object> message) {
        System.out.println("Received Order Event: "+ message);

        // update database
        //send notification
        // send emails
        // generate invoice
        // send seller notification
    }

}
