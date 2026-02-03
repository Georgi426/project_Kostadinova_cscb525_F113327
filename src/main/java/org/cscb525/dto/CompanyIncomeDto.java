package org.cscb525.dto;

import java.math.BigDecimal;

public class CompanyIncomeDto {
    private String companyName;
    private BigDecimal totalIncome;

    public CompanyIncomeDto(String companyName, BigDecimal totalIncome) {
        this.companyName = companyName;
        this.totalIncome = totalIncome;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Override
    public String toString() {
        return "Company: " + companyName + ", Total Income: " + totalIncome;
    }
}
