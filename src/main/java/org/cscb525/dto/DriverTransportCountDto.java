package org.cscb525.dto;

public class DriverTransportCountDto {
    private String driverName;
    private Long transportCount;

    public DriverTransportCountDto(String driverName, Long transportCount) {
        this.driverName = driverName;
        this.transportCount = transportCount;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getTransportCount() {
        return transportCount;
    }

    public void setTransportCount(Long transportCount) {
        this.transportCount = transportCount;
    }

    @Override
    public String toString() {
        return "Driver: " + driverName + ", Transports: " + transportCount;
    }
}
