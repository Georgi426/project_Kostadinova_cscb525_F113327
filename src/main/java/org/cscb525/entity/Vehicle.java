package org.cscb525.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    public enum VehicleType {
        BUS, TRUCK, TANKER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String model;

    @Column(name = "license_plate", length = 20, unique = true)
    private String licensePlate;

    @Column(name = "production_year")
    private Integer productionYear;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }


    @Column(name = "required_qualification")
    @NotBlank(message = "Required qualification cannot be empty")
    @Size(max = 255, message = "Qualification cannot be longer than 255 characters")
    private String requiredQualification;

    @Column(name = "max_passengers")
    private Integer maxPassengers;

    @Column(name = "max_cargo_weight")
    private Double maxCargoWeight;


    @NotNull(message = "Vehicle must be associated with a company.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Vehicle(VehicleType type, String requiredQualification, Company company) {
        this.type = type;
        this.requiredQualification = requiredQualification;
        this.company = company;
    }

    @Override
    public String toString() {
        return type.toString() + " #" + id + " (Req: " + requiredQualification + ")";
    }
}
