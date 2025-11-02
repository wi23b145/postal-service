
package org.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfig {
    //@Primary
    @Bean
    public Queue packageQueue(@Value("${app.queues.pack}") String name) {
        // durable=true, damit die Queue auf dem Broker bestehen bleibt
        return new Queue(name, true);
    }

    @Bean
    public Queue packageQueue2(@Value("Halloooo") String name) {
        // durable=true, damit die Queue auf dem Broker bestehen bleibt
        return new Queue(name, true);
    }
}