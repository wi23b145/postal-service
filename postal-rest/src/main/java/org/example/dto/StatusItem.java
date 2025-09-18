package org.example.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StatusItem(
        String type,          // "LETTER" | "PACKAGE"
        UUID id,
        String name,
        String country,       // LETTER: AT/DE/CH, PACKAGE: null
        BigDecimal weightKg,  // PACKAGE: Wert, LETTER: null
        String status         // waiting | send | rejected
) {}
