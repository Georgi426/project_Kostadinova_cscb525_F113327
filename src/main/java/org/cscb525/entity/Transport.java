package org.cscb525.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transports")
@Getter
@Setter
@NoArgsConstructor
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TransportType {
        CARGO, PASSENGER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type")
    private TransportType type;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "price_per_kg")
    private BigDecimal pricePerKg;

    @Column(name = "price_per_passenger")
    private BigDecimal pricePerPassenger;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;


    @NotNull(message = "Transport must be associated with a transport company.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull(message = "Transport must be associated with a client.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "Transport must be associated with a vehicle.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull(message = "Transport must be associated with a driver.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Employee driver;

    @Column(nullable = false)
    @NotBlank(message = "Departure address is required.")
    @Size(max = 255, message = "Departure address cannot be longer than 255 characters.")
    private String fromLocation;

    @Column(nullable = false)
    @NotBlank(message = "Arrival address is required.")
    @Size(max = 255, message = "Arrival address cannot be longer than 255 characters.")
    private String toLocation;

    @Column(nullable = false)
    @NotNull(message = "Departure date and time are required.")
    private LocalDateTime departureDateTime;

    @Column(nullable = false)
    @NotNull(message = "Arrival date and time are required.")
    private LocalDateTime arrivalDateTime;


    @Column(length = 500)
    @Size(max = 500, message = "Cargo description cannot be longer than 500 characters.")
    private String cargoDescription;

    @PositiveOrZero(message = "Cargo weight cannot be negative.")
    private Double cargoWeight;


    @Column(nullable = false)
    @NotNull(message = "Transport price is required.")
    @PositiveOrZero(message = "Transport price cannot be negative.")
    private BigDecimal price;

    @Override
    public String toString() {
        return "Transport: " + fromLocation + " -> " + toLocation;
    }
}
