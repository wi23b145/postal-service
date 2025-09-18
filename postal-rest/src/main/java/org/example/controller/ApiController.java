package org.example.controller;

import org.example.dto.StatusItem;
import org.example.entities.LetterEntity;
import org.example.entities.PackageEntity;
import org.example.repo.LetterRepository;
import org.example.repo.PackageRepository;
import org.example.service.MessagePublisher;
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
    private final MessagePublisher publisher; // statt AmqpTemplate direkt

    public ApiController(
            LetterRepository letterRepo,
            PackageRepository packageRepo,
            MessagePublisher publisher
    ) {
        this.letterRepo = letterRepo;
        this.packageRepo = packageRepo;
        this.publisher = publisher;
    }

    // --- LETTER --------------------------------------------------------------

    @PostMapping("/letter/{country}/{name}")
    public ResponseEntity<UUID> sendLetter(
            @PathVariable String country,
            @PathVariable String name
    ) {
        // kein Validieren: alles annehmen
        LetterEntity e = new LetterEntity();
        e.setId(UUID.randomUUID());
        e.setName(safe(name));
        e.setCountry(safe(country).toUpperCase()); // nur hübsch machen
        e.setStatus("waiting");

        letterRepo.save(e);
        publisher.publishLetter(e.getId()); // nur ID in Queue
        return ResponseEntity.ok(e.getId());
    }

    // --- PACKAGE -------------------------------------------------------------

    @PostMapping("/package/{weight}/{name}")
    public ResponseEntity<UUID> sendPackage(
            @PathVariable BigDecimal weight,
            @PathVariable String name
    ) {
        PackageEntity p = new PackageEntity();
        p.setId(UUID.randomUUID());
        p.setName(safe(name));
        p.setWeightKg(weight);
        p.setStatus("waiting");

        packageRepo.save(p);
        publisher.publishPackage(p.getId()); // nur ID in Queue
        return ResponseEntity.ok(p.getId());
    }

    // --- STATUS --------------------------------------------------------------

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

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
