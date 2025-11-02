module postal.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    opens org.example.dto to com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;      // für FXML-Controller
    opens org.example.model to javafx.base;

    exports org.example;
    exports org.example.model;
}
