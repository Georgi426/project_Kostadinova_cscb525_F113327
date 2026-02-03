package org.cscb525.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String name;
    private String qualification;
    private BigDecimal salary;
    private String companyName;

    @Override
    public String toString() {
        return String.format("Employee: %s, Qual: %s, Salary: %s, Company: %s", name, qualification, salary, companyName);
    }
}
