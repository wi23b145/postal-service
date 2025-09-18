// package-worker/src/main/java/org/example/config/RabbitConfig.java
package org.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue packageQueue(@Value("${app.queues.pack}") String name) {
        // durable=true, damit die Queue auf dem Broker bestehen bleibt
        return new Queue(name, true);
    }
}
