package org.cscb525.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TotalIncomeDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalAmount;

    public TotalIncomeDto(LocalDate startDate, LocalDate endDate, BigDecimal totalAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return String.format("Total Income (%s to %s): %s",
                startDate != null ? startDate : "Start",
                endDate != null ? endDate : "End",
                totalAmount);
    }
}
