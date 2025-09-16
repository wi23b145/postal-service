package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class PostalGuiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = PostalGuiApplication.class.getResource("/postal.fxml");
        if (fxml == null) throw new IllegalStateException("postal.fxml nicht gefunden");
        Scene scene = new Scene(new FXMLLoader(fxml).load(), 1000, 600);
        stage.setTitle("Postal Service");
        stage.setScene(scene);
        stage.show();
    }
}

