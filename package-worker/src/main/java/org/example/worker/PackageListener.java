package org.example.worker;

import org.example.entities.PackageEntity;
import org.example.repo.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class PackageListener {
    private static final Logger log = LoggerFactory.getLogger(PackageListener.class);

    private final PackageRepository repo;

    public PackageListener(PackageRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = "${app.queues.pack}")
    public void onMessage(String idAsString) {
        try {
            UUID id = UUID.fromString(idAsString);
            log.info("Package message received: {}", id);

            Optional<PackageEntity> opt = repo.findById(id);
            if (opt.isEmpty()) {
                log.warn("Package {} not found in DB", id);
                return;
            }

            PackageEntity p = opt.get();
            BigDecimal w = p.getWeightKg() == null ? BigDecimal.ZERO : p.getWeightKg();
            String newStatus = w.compareTo(new BigDecimal("25.0")) < 0 ? "sent" : "rejected_overweight";

            Thread.sleep(300); // mini Demo-Verzögerung

            p.setStatus(newStatus);
            repo.save(p);
            log.info("Package {} set to status '{}'", id, newStatus);
        } catch (Exception e) {
            log.error("Failed to process package message '{}': {}", idAsString, e.getMessage(), e);
        }
    }
}
