package org.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.queues.letter}") private String letterQueue;
    @Value("${app.queues.pack}")   private String packQueue;

    @Bean public Queue letterQueue() { return new Queue(letterQueue, true); }
    @Bean public Queue packQueue()   { return new Queue(packQueue, true); }
}
