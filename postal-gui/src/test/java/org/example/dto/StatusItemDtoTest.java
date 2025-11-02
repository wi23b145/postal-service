package org.example.dto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatusItemDtoTest {
    @Test
    void createsLetterDto_andExposesComponents() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        StatusItemDto dto = new StatusItemDto("LETTER", id, "Alice", "AT", null, "waiting");

        assertEquals("LETTER", dto.type());
        assertEquals(id, dto.id());
        assertEquals("Alice", dto.name());
        assertEquals("AT", dto.country());
        assertNull(dto.weightKg());                 // LETTER: null
        assertEquals("waiting", dto.status());
    }

    @Test
    void createsPackageDto_andExposesComponents() {
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
        StatusItemDto dto = new StatusItemDto("PACKAGE", id, "Box", null, new BigDecimal("2.50"), "send");

        assertEquals("PACKAGE", dto.type());
        assertEquals(id, dto.id());
        assertEquals("Box", dto.name());
        assertNull(dto.country());                  // PACKAGE: null
        assertEquals(0, new BigDecimal("2.50").compareTo(dto.weightKg()));
        assertEquals("send", dto.status());
    }

    @Test
    void equalsAndHashCodeByValue() {
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
        StatusItemDto a = new StatusItemDto("LETTER", id, "Alice", "AT", null, "waiting");
        StatusItemDto b = new StatusItemDto("LETTER", id, "Alice", "AT", null, "waiting");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}

