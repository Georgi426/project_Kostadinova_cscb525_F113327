package org.cscb525.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Company name cannot be empty")
    @Size(min = 2, max = 50, message = "Company name must be between 2 and 50 characters")
    @Size(min = 2, max = 50, message = "Company name must be between 2 and 50 characters")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "Address cannot be longer than 500 characters")
    private String address;

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Client> clients = new HashSet<>();

    public Company(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setCompany(this);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setCompany(this);
    }

    public void addClient(Client client) {
        clients.add(client);
        client.setCompany(this);
    }
}
