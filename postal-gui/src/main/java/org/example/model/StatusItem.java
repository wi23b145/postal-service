package org.example.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class StatusItem {
    private final StringProperty type = new SimpleStringProperty("");
    private final StringProperty id = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty country = new SimpleStringProperty("");
    private final StringProperty weight = new SimpleStringProperty("");
    private final StringProperty status = new SimpleStringProperty("");

    public String getType() { return type.get(); }
    public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCountry() { return country.get(); }
    public String getWeight() { return weight.get(); }
    public String getStatus() { return status.get(); }

    public StringProperty typeProperty() { return type; }
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty countryProperty() { return country; }
    public StringProperty weightProperty() { return weight; }
    public StringProperty statusProperty() { return status; }

    public void setType(String v) { type.set(v); }
    public void setId(String v) { id.set(v); }
    public void setName(String v) { name.set(v); }
    public void setCountry(String v) { country.set(v); }
    public void setWeight(String v) { weight.set(v); }
    public void setStatus(String v) { status.set(v); }
}
