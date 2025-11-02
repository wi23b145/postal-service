package org.example;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostalGuiApplicationTest {
    @BeforeAll
    static void initFxToolkit() throws Exception {
        // Startet das JavaFX Toolkit genau einmal pro Testlauf
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            // Toolkit wurde bereits gestartet (z.B. durch anderen Test) – ignorieren
            latch.countDown();
        }
        latch.await();
    }

    @Test
    void fxmlFileExistsAndLoads_onFxThread() throws Exception {
        var url = PostalGuiApplication.class.getResource("/postal.fxml");
        assertNotNull(url, "postal.fxml sollte unter src/main/resources liegen");

        var rootRef = new AtomicReference<Parent>();
        var done = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                var loader = new FXMLLoader(url);
                rootRef.set(loader.load());
            } catch (Exception e) {
                // im Test sauber fehlschlagen
                throw new RuntimeException(e);
            } finally {
                done.countDown();
            }
        });

        done.await();

        assertNotNull(rootRef.get(), "FXML sollte erfolgreich geladen werden");
    }

}