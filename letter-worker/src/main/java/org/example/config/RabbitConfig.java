
package org.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfig {
    //@Primary

    public Queue letterQueue(@Value("${app.queues.letter}") String name) {
        return new Queue(name, true);
    }



}