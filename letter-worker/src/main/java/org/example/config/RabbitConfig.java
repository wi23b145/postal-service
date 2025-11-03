
package org.example.config;

import org.example.repo.LetterRepository;
import org.example.worker.LetterListener;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue letterQueue(@Value("${app.queues.letter}") String name) {

        return new Queue(name, true);
    }


}
