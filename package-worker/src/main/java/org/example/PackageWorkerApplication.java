package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableRabbit
@EntityScan("org.example.entities")
@EnableJpaRepositories("org.example.repo")
@SpringBootApplication
public class PackageWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PackageWorkerApplication.class, args);
    }
}
