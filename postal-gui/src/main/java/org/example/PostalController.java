package org.example;

import org.example.dto.StatusItemDto;
import org.example.model.StatusItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostalController {
    // FXML-Injektionen
    @FXML private TextField letterNameField;
    @FXML private TextField letterCountryField;
    @FXML private TextField packageNameField;
    @FXML private TextField packageWeightField;
    @FXML private TableView<StatusItem> statusTable;
    @FXML private TableColumn<StatusItem, String> typeCol;
    @FXML private TableColumn<StatusItem, String> idCol;
    @FXML private TableColumn<StatusItem, String> nameCol;
    @FXML private TableColumn<StatusItem, String> countryCol;
    @FXML private TableColumn<StatusItem, String> weightCol;
    @FXML private TableColumn<StatusItem, String> statusCol;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = System.getenv().getOrDefault("REST_URL", "http://localhost:8082");

    @FXML
    public void initialize() {
        // Table-Columns an Model-Properties binden
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));


        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setText("ID");
        idCol.setSortable(false); // stabil, unabhängig von anderer Sortierung
        idCol.setCellFactory(col -> new TableCell<StatusItem, String>() {
            @Override
            protected void updateItem(String uuid, boolean empty) {
                super.updateItem(uuid, empty);
                if (empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(String.valueOf(getIndex() + 1)); // 1,2,3,...
                    setTooltip((uuid == null || uuid.isBlank()) ? null : new Tooltip(uuid));
                }
            }
        });
    }

    @FXML
    private void handleSendLetter() {
        String name = urlEncode(letterNameField.getText());
        String country = urlEncode(letterCountryField.getText());
        if (name.isBlank() || country.isBlank()) {
            alert("Provide name and country"); return;
        }
        doPost(baseUrl + "/api/letter/" + country + "/" + name);
    }

    @FXML
    private void handleSendPackage() {
        String name = urlEncode(packageNameField.getText());
        String weight = urlEncode(packageWeightField.getText());
        if (name.isBlank() || weight.isBlank()) {
            alert("Provide name and weight"); return;
        }
        doPost(baseUrl + "/api/package/" + weight + "/" + name);
    }

    @FXML
    private void handleRefresh() { refreshStatus(); }

    // --- HTTP/JSON Hilfen ---

    private void doPost(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() >= 200 && res.statusCode() < 300) {
                refreshStatus();
            } else {
                alert("Error: " + res.statusCode() + "\n" + res.body());
            }
        } catch (Exception ex) {
            alert("POST failed: " + ex.getMessage());
        }
    }

    private void refreshStatus() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/status"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<StatusItem> items = parseItems(res.body());
                statusTable.getItems().setAll(items);
            } else {
                alert("GET /status failed: " + res.statusCode());
            }
        } catch (Exception ex) {
            alert("GET failed: " + ex.getMessage());
        }
    }

    List<StatusItem> parseItems(String json) {
        try {
            List<StatusItemDto> raw = mapper.readValue(
                    json, new com.fasterxml.jackson.core.type.TypeReference<List<StatusItemDto>>() {}
            );
            return raw.stream().map(m -> {
                var s = new StatusItem();
                s.setType(m.type());
                s.setId(m.id() == null ? null : m.id().toString());
                s.setName(m.name());
                s.setCountry(m.country());
                var w = m.weightKg();
                s.setWeight(w == null ? "" : w.stripTrailingZeros().toPlainString());
                s.setStatus(m.status());
                return s;
            }).toList();
        } catch (Exception e) {
            // Wichtig: KEIN Alert hier!
            return List.of();
        }
    }

    private String str(Object o) { return o == null ? "" : String.valueOf(o); }

    private void alert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private String urlEncode(String s) {
        return java.net.URLEncoder.encode(s == null ? "" : s.trim(), StandardCharsets.UTF_8);
    }
}
