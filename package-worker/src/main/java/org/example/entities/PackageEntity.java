package org.example.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "packages") // muss zum Tabellennamen in postal-rest passen
public class PackageEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    private String status;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
