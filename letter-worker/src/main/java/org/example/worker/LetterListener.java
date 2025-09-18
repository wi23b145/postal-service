package org.example.worker;

import org.example.entities.LetterEntity;
import org.example.repo.LetterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class LetterListener {
    private static final Logger log = LoggerFactory.getLogger(LetterListener.class);
    private final LetterRepository repo;

    private static final Set<String> ALLOWED = Set.of("AT","DE","CH");

    public LetterListener(LetterRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = "${app.queues.letter}")
    public void onMessage(String idAsString) {
        try {
            UUID id = UUID.fromString(idAsString);
            log.info("Letter message received: {}", id);

            Optional<LetterEntity> opt = repo.findById(id);
            if (opt.isEmpty()) {
                log.warn("Letter {} not found in DB", id);
                return;
            }

            LetterEntity l = opt.get();

            // Demo-Regel: AT/DE/CH -> sent, sonst rejected_unknown_country
            String country = l.getCountry() == null ? "" : l.getCountry().trim().toUpperCase();
            String newStatus = ALLOWED.contains(country) ? "sent" : "rejected_unknown_country";

            // kleine Demo-Verzögerung
            Thread.sleep(300);

            l.setStatus(newStatus);
            repo.save(l);

            log.info("Letter {} set to status '{}'", id, newStatus);
        } catch (Exception e) {
            log.error("Failed to process letter message '{}': {}", idAsString, e.getMessage(), e);
        }
    }
}
