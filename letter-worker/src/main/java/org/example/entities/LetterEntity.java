package org.example.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "letters")
public class LetterEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(length = 5)
    private String country;

    private String status;

    // --- getters/setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
