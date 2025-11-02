package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.example.dto.StatusItemDto;
import org.example.model.StatusItem;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

class PostalControllerTest {
    @Test
    void parsesLetterAndPackageCorrectly() {
        String json = """
          [
            {"type":"LETTER","id":"11111111-1111-1111-1111-111111111111","name":"Alice","country":"AT","weightKg":null,"status":"waiting"},
            {"type":"PACKAGE","id":"22222222-2222-2222-2222-222222222222","name":"Box","country":null,"weightKg":2.50,"status":"sent"}
          ]
        """;

        PostalController c = new PostalController();
        List<StatusItem> items = c.parseItems(json);

        assertEquals(2, items.size());
        StatusItem letter = items.get(0);
        assertEquals("LETTER", letter.getType());
        assertEquals("11111111-1111-1111-1111-111111111111", letter.getId());
        assertEquals("AT", letter.getCountry());
        assertEquals("", letter.getWeight()); // null -> ""

        StatusItem pack = items.get(1);
        assertEquals("PACKAGE", pack.getType());
        assertEquals("2.5", pack.getWeight()); // BigDecimal -> String
        assertEquals("sent", pack.getStatus());
    }

    @Test
    void returnsEmptyListOnInvalidJson() {
        PostalController c = new PostalController();
        List<StatusItem> items = c.parseItems("not-json");
        assertTrue(items.isEmpty());
    }
}

