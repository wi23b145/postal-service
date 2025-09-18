package org.example.controller;

import org.example.dto.StatusItem;
import org.example.entities.LetterEntity;
import org.example.entities.PackageEntity;
import org.example.repo.LetterRepository;
import org.example.repo.PackageRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final LetterRepository letterRepo;
    private final PackageRepository packageRepo;
    private final AmqpTemplate amqpTemplate;

    private final String letterQueue;
    private final String packageQueue;

    public ApiController(
            LetterRepository letterRepo,
            PackageRepository packageRepo,
            AmqpTemplate amqpTemplate,
            @Value("${app.queues.letter}") String letterQueue,
            @Value("${app.queues.pack}") String packageQueue
    ) {
        this.letterRepo = letterRepo;
        this.packageRepo = packageRepo;
        this.amqpTemplate = amqpTemplate;
        this.letterQueue = letterQueue;
        this.packageQueue = packageQueue;
    }

    // POST /api/letter/{country}/{name}
    @PostMapping("/letter/{country}/{name}")
    public ResponseEntity<UUID> sendLetter(
            @PathVariable("country") String country,
            @PathVariable("name")    String name
    ) {
        LetterEntity e = new LetterEntity();
        e.setId(UUID.randomUUID());
        e.setName(name);
        e.setCountry(country.toUpperCase());
        e.setStatus("waiting");
        letterRepo.save(e);

        // Nur die ID als Message in Queue
        amqpTemplate.convertAndSend(letterQueue, e.getId().toString());
        return ResponseEntity.ok(e.getId());
    }

    @PostMapping("/package/{weight}/{name}")
    public ResponseEntity<UUID> sendPackage(
            @PathVariable("weight") BigDecimal weight,
            @PathVariable("name")   String name
    ) {
        PackageEntity p = new PackageEntity();
        p.setId(UUID.randomUUID());
        p.setName(name);
        p.setWeightKg(weight);
        p.setStatus("waiting");
        packageRepo.save(p);

        amqpTemplate.convertAndSend(packageQueue, p.getId().toString());
        return ResponseEntity.ok(p.getId());
    }


    // GET /api/status
    @GetMapping("/status")
    public ResponseEntity<List<StatusItem>> status() {
        List<StatusItem> out = new ArrayList<>();

        letterRepo.findAll().forEach(l ->
                out.add(new StatusItem("LETTER", l.getId(), l.getName(), l.getCountry(), null, l.getStatus()))
        );
        packageRepo.findAll().forEach(p ->
                out.add(new StatusItem("PACKAGE", p.getId(), p.getName(), null, p.getWeightKg(), p.getStatus()))
        );

        return ResponseEntity.ok(out);
    }
}
