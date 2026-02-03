package org.cscb525.dto;

import java.math.BigDecimal;

public class DriverIncomeDto {
    private String driverName;
    private BigDecimal income;

    public DriverIncomeDto(String driverName, BigDecimal income) {
        this.driverName = driverName;
        this.income = income;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "Driver: " + driverName + ", Income: " + income;
    }
}
