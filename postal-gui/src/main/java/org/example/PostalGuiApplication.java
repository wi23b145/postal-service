package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PostalGuiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        var url = PostalGuiApplication.class.getResource("/postal.fxml"); // <- aus src/main/resources
        if (url == null) {
            throw new IllegalStateException("postal.fxml nicht gefunden (erwartet unter src/main/resources)");
        }

        var root = new FXMLLoader(url).load();
        var scene = new Scene((Parent) root, 1000, 600);
        stage.setTitle("Postal Service");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
