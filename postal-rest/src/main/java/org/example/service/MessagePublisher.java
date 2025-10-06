package org.example.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessagePublisher {

    private final RabbitTemplate rabbit;
    private final String letterQueue;
    private final String packQueue;

    public MessagePublisher(
            RabbitTemplate rabbit,
            @Value("${app.queues.letter}") String letterQueue,
            @Value("${app.queues.pack}") String packQueue
    ) {
        this.rabbit = rabbit;
        this.letterQueue = letterQueue;
        this.packQueue = packQueue;
    }

    public void publishLetter(UUID id)  { rabbit.convertAndSend(letterQueue, id.toString()); }
    public void publishPackage(UUID id) { rabbit.convertAndSend(packQueue,  id.toString()); }
}