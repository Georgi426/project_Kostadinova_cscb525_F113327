package org.cscb525.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Employee name cannot be empty")
    @Size(max = 255, message = "Employee name cannot be longer than 255 characters")
    private String name;


    @NotBlank(message = "Qualification required")
    @Size(max = 255, message = "Qualification cannot be longer than 255 characters")
    private String qualification; 
    // записва като един низ –>logik transportservice driver.getQualification()

    @PositiveOrZero(message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "Employee must be associated with a company.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Employee(String name, String qualification, BigDecimal salary, Company company) {
        this.name = name;
        this.qualification = qualification;
        this.salary = salary;
        this.company = company;
    }

    @Override
    public String toString() {
        return name + " (" + qualification + ")";
    }
}
