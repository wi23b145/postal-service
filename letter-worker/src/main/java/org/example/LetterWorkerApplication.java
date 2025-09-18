package org.example;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableRabbit
@EntityScan(basePackages = "org.example.entities")
@EnableJpaRepositories(basePackages = "org.example.repo")
public class LetterWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterWorkerApplication.class, args);
    }

    // Queue deklarieren -> verhindert "NOT_FOUND - no queue ..." beim Start
    @Bean
    Queue letterQueue(@Value("${app.queues.letter}") String name) {
        return new Queue(name, true); // durable
    }

    // Admin bean, damit die Queue beim Start auch wirklich deklariert wird
    @Bean
    AmqpAdmin amqpAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }
}
